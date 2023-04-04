package com.bestDate.presentation.main

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.data.utils.notifications.PusherCenter
import com.bestDate.db.dao.UserDao
import com.bestDate.db.dao.UserSettingsDao
import com.bestDate.db.entity.QuestionnaireDB
import com.bestDate.network.remote.AuthRemoteData
import com.bestDate.network.remote.UserRemoteData
import com.bestDate.presentation.auth.AuthUseCase
import com.bestDate.presentation.main.chats.ChatListUseCase
import com.bestDate.presentation.main.duels.DuelsUseCase
import com.bestDate.presentation.main.guests.GuestsUseCase
import com.bestDate.presentation.main.userProfile.invitationList.InvitationListUseCase
import com.bestDate.presentation.main.userProfile.likesList.LikesListUseCase
import com.bestDate.presentation.main.userProfile.matchesList.MatchesListUseCase
import com.bestDate.presentation.main.userProfile.myDuels.MyDuelsUseCase
import com.bestDate.presentation.main.userProfile.settings.bockedUsers.BlockedUserUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUseCase @Inject constructor(
    private val userDao: UserDao,
    private val authRemoteData: AuthRemoteData,
    private val userRemoteData: UserRemoteData,
    private val likesListUseCase: LikesListUseCase,
    private val matchesListUseCase: MatchesListUseCase,
    private val myDuelsUseCase: MyDuelsUseCase,
    private val invitationUseCase: InvitationListUseCase,
    private val guestsUseCase: GuestsUseCase,
    private val userSettingsDao: UserSettingsDao,
    private val blockedUserUseCase: BlockedUserUseCase,
    private val duelsUseCase: DuelsUseCase,
    private val chatListUseCase: ChatListUseCase,
    private val authUseCase: AuthUseCase,
    private val pusherCenter: PusherCenter,
    private val preferencesUtils: PreferencesUtils
) {

    val getMyUser = userDao.getUserFlow()
    var coinsCount: MutableLiveData<String?> = MutableLiveData("0")

    suspend fun refreshUser() {
        val response = userRemoteData.getUserData()
        if (response.isSuccessful) {
            response.body()?.data?.let {
                userDao.validate(it)
                coinsCount.postValue(it.coins)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun logout() {
        authRemoteData.logout()
        pusherCenter.disconnect()
        clearUserData()
    }

    fun clearUserData() {
        userDao.delete()
        userSettingsDao.delete()
        likesListUseCase.clearData()
        duelsUseCase.clearData()
        matchesListUseCase.clearData()
        myDuelsUseCase.clearData()
        invitationUseCase.clearData()
        guestsUseCase.clearData()
        blockedUserUseCase.clearData()
        chatListUseCase.clearData()
        authUseCase.clearData()
        preferencesUtils.saveString(Preferences.ACCESS_TOKEN, "")
        preferencesUtils.saveString(Preferences.REFRESH_TOKEN, "")
        preferencesUtils.saveLong(Preferences.ARG_EXPIRES_AT, 0L)
        preferencesUtils.saveString(Preferences.FILTER_LOCATION, "all")
        preferencesUtils.saveString(Preferences.FILTER_STATUS, "all")
    }

    suspend fun saveQuestionnaire(questionnaire: QuestionnaireDB) {
        val response = userRemoteData.saveQuestionnaire(questionnaire)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun changeLanguage(language: String) {
        val response = userRemoteData.changeLanguage(language)
        if (response.isSuccessful) {
            response.body()?.data?.let {
                userDao.validate(it)
                coinsCount.postValue(it.coins)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun deleteUserProfile() {
        userRemoteData.deleteUserProfile()
        pusherCenter.disconnect()
        clearUserData()
    }
}