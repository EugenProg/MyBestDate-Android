package com.bestDate.presentation.main

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.FilterOptions
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.ShortUserData
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.db.dao.UserDao
import com.bestDate.db.dao.UserSettingsDao
import com.bestDate.db.entity.QuestionnaireDB
import com.bestDate.db.entity.UserDB
import com.bestDate.network.remote.AuthRemoteData
import com.bestDate.network.remote.UserRemoteData
import com.bestDate.presentation.main.chats.ChatListUseCase
import com.bestDate.presentation.main.guests.GuestsUseCase
import com.bestDate.presentation.main.duels.DuelsUseCase
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
    private val preferencesUtils: PreferencesUtils
) {

    val getMyUser = userDao.getUserFlow()
    var usersList: MutableList<ShortUserData>? = mutableListOf()
    var perPage: Int = 10
    var currentPage: Int = 1
    var lastPage: Int = 1
    var total: Int = 0

    suspend fun refreshUser() {
        val response = userRemoteData.getUserData()
        if (response.isSuccessful) {
            response.body()?.data?.let {
                userDao.validate(it)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun logout() {
        authRemoteData.logout()
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

    suspend fun getUsers(filters: FilterOptions) {
        val response = userRemoteData.getUsers(filters, 1)
        if (response.isSuccessful) {
            usersList = response.body()?.data
            perPage = response.body()?.meta?.per_page ?: 0
            currentPage = response.body()?.meta?.current_page ?: 0
            lastPage = response.body()?.meta?.last_page ?: 0
            total = response.body()?.meta?.total ?: 0
        } else {
            throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
        }
    }

    suspend fun getUsersPaged(filters: FilterOptions) {
        currentPage++

        if (lastPage < currentPage) return
        val response = userRemoteData.getUsers(filters, currentPage)
        if (response.isSuccessful) {
            val list = mutableListOf<ShortUserData>()
            usersList?.let { list.addAll(it) }
            list.addAll(response.body()?.data ?: mutableListOf())
            usersList = list
        } else {
            throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
        }
    }

    fun clearPagingData() {
        usersList = mutableListOf()
        perPage = 10
        currentPage = 1
        lastPage = 1
        total = 0
    }

    suspend fun changeLanguage(language: String) {
        val response = userRemoteData.changeLanguage(language)
        if (response.isSuccessful) {
            val user = response.body()?.data
            userDao.validate(user ?: UserDB(id = 0))
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun deleteUserProfile() {
        userRemoteData.deleteUserProfile()
        clearUserData()
    }
}