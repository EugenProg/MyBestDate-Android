package com.bestDate.presentation.main

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.InternalException
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.data.utils.notifications.PusherCenter
import com.bestDate.db.dao.UserDao
import com.bestDate.db.dao.UserSettingsDao
import com.bestDate.db.entity.QuestionnaireDB
import com.bestDate.db.entity.UserDB
import com.bestDate.network.remote.AuthRemoteData
import com.bestDate.network.remote.UserRemoteData
import com.bestDate.presentation.auth.AuthUseCase
import com.bestDate.presentation.main.chats.ChatListUseCase
import com.bestDate.presentation.main.duels.DuelsUseCase
import com.bestDate.presentation.main.guests.GuestsUseCase
import com.bestDate.presentation.main.matches.MatchUseCase
import com.bestDate.presentation.main.search.FilterType
import com.bestDate.presentation.main.search.GenderFilter
import com.bestDate.presentation.main.search.SearchUseCase
import com.bestDate.presentation.main.userProfile.invitationList.InvitationListUseCase
import com.bestDate.presentation.main.userProfile.settings.bockedUsers.BlockedUserUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUseCase @Inject constructor(
    private val userDao: UserDao,
    private val authRemoteData: AuthRemoteData,
    private val userRemoteData: UserRemoteData,
    private val guestsUseCase: GuestsUseCase,
    private val userSettingsDao: UserSettingsDao,
    private val blockedUserUseCase: BlockedUserUseCase,
    private val duelsUseCase: DuelsUseCase,
    private val chatListUseCase: ChatListUseCase,
    private val authUseCase: AuthUseCase,
    private val searchUseCase: SearchUseCase,
    private val matchUseCase: MatchUseCase,
    private val pusherCenter: PusherCenter,
    private val invitationUseCase: InvitationListUseCase,
    private val preferencesUtils: PreferencesUtils
) {

    val getMyUser = userDao.getUserFlow()
    var userMainPhotoUrl: MutableLiveData<String?> = MutableLiveData()
    var coinsCount: MutableLiveData<String?> = MutableLiveData("0")
    var hasNewLikes: MutableLiveData<Boolean> = MutableLiveData(false)
    var hasNewMatches: MutableLiveData<Boolean> = MutableLiveData(false)
    var hasNewDuels: MutableLiveData<Boolean> = MutableLiveData(false)

    suspend fun refreshUser() {
        val response = userRemoteData.getUserData()
        if (response.isSuccessful) {
            response.body()?.data?.let {
                setUser(it)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    private fun setUser(user: UserDB) {
        userDao.validate(user)
        userMainPhotoUrl.postValue(user.getMainPhotoThumbUrl())
        setUserGenderFilter(user.getGenderFilter())
        coinsCount.postValue(user.coins)
        guestsUseCase.hasNewGuests.postValue(user.new_guests.orZero > 0)
        invitationUseCase.hasNewInvitations.postValue(user.new_invitations.orZero > 0)
        hasNewLikes.postValue(user.new_likes.orZero > 0)
        hasNewMatches.postValue(user.new_matches.orZero > 0)
        hasNewDuels.postValue(user.new_duels.orZero > 0)
        preferencesUtils.saveInt(Preferences.SENT_MESSAGES_TODAY, user.sent_messages_today.orZero)
        preferencesUtils.saveInt(
            Preferences.SENT_INVITATIONS_TODAY,
            user.sent_invitations_today.orZero
        )
    }

    suspend fun logout() {
        authRemoteData.logout()
        pusherCenter.disconnect()
        clearUserData()
    }

    fun clearUserData() {
        userDao.delete()
        userSettingsDao.delete()
        duelsUseCase.clearData()
        guestsUseCase.clearData()
        blockedUserUseCase.clearData()
        chatListUseCase.clearData()
        authUseCase.clearData()
        searchUseCase.clearPagingData()
        matchUseCase.clearData()
        preferencesUtils.saveString(Preferences.ACCESS_TOKEN, "")
        preferencesUtils.saveString(Preferences.REFRESH_TOKEN, "")
        preferencesUtils.saveLong(Preferences.ARG_EXPIRES_AT, 0L)
        preferencesUtils.saveString(Preferences.FILTER_LOCATION, FilterType.ALL.serverName)
        preferencesUtils.saveString(Preferences.FILTER_STATUS, FilterType.NOT_SELECTED.serverName)
        preferencesUtils.saveString(Preferences.FILTER_GENDER, "")
        preferencesUtils.saveInt(Preferences.SENT_MESSAGES_TODAY, 0)
        preferencesUtils.saveInt(Preferences.SENT_INVITATIONS_TODAY, 0)
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
                setUser(it)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    private fun setUserGenderFilter(filter: GenderFilter) {
        if (preferencesUtils.getString(Preferences.FILTER_GENDER).isBlank()) {
            preferencesUtils.saveString(Preferences.FILTER_GENDER, filter.name)
        }
    }

    suspend fun deleteUserProfile() {
        userRemoteData.deleteUserProfile()
        pusherCenter.disconnect()
        clearUserData()
    }
}