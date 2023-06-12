package com.bestDate.network.remote

import com.bestDate.data.model.*
import com.bestDate.db.entity.QuestionnaireDB
import com.bestDate.network.services.UserService
import com.bestDate.view.alerts.BuyDialogType
import javax.inject.Inject

class UserRemoteData @Inject constructor(
    private val service: UserService
) {

    suspend fun getUserData() = service.getUserData()

    suspend fun getUserById(id: Int) = service.getUserById(id)

    suspend fun saveQuestionnaire(questionnaire: QuestionnaireDB) =
        service.saveQuestionnaire(questionnaire)

    suspend fun getUserLikes(page: Int) = service.getLikesList(page)

    suspend fun like(body: LikesBody) = service.like(body)

    suspend fun getUsers(filters: FilterOptions, page: Int) =
        service.getUsers(filters, page)

    suspend fun getUserMatches(page: Int) = service.getMatchesList(page)

    suspend fun getUsersForMatch() = service.getUsersForMatch()

    suspend fun matchAction(userId: Int) = service.matchAction(MatchActionRequest(userId))

    suspend fun getMyDuels(page: Int) = service.getMyDuels(page)

    suspend fun getBlockedUsers() = service.getBlockedUsers()

    suspend fun blockUser(id: Int) = service.blockUser(id)

    suspend fun unlockUser(id: Int) = service.unlockUser(id)

    suspend fun complain(id: Int) = service.complain(MatchActionRequest(id))

    suspend fun changeLanguage(language: String) = service.changeLanguage(RequestLanguage(language))

    suspend fun getUserInvitations(filter: InvitationFilter, page: Int) =
        service.getInvitations(
            UserInvitationRequest(filter.serverName), page
        )

    suspend fun updateUserData(userRequest: UpdateUserRequest) = service.updateUserData(userRequest)

    suspend fun sendEmailCode(email: String) = service.sendEmailCode(EmailRequest(email))

    suspend fun saveUserEmail(email: String, code: String) =
        service.saveUserEmail(ConfirmRequest(email = email, code = code))

    suspend fun sendPhoneCode(phone: String) = service.sendPhoneCode(PhoneRequest(phone))

    suspend fun saveUserPhone(phone: String, code: String) =
        service.saveUserPhone(ConfirmRequest(phone = phone, code = code))

    suspend fun changePassword(oldPass: String, newPass: String) =
        service.updateUserPassword(UpdatePasswordRequest(oldPass, newPass, newPass))

    suspend fun saveUserLocation(locationRequest: SaveUserLocationRequest) =
        service.saveUserLocation(locationRequest)

    suspend fun getUserSettings() = service.getUserSettings()

    suspend fun updateUserSettings(type: SettingsType, checked: Boolean) =
        service.updateUserSettings(type.getSettingsRequest(checked))

    suspend fun deleteUserProfile() = service.deleteUserProfile()

    suspend fun saveMessagingDeviceToken(token: String) =
        service.saveMessagingDeviceToken(SaveDeviceTokenRequest(token = token))

    suspend fun withdrawCoins(type: BuyDialogType) =
        service.withdrawCoins(WithdrawCoinsRequest(type.buyName))
}