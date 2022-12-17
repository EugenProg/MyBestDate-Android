package com.bestDate.network.remote

import com.bestDate.data.model.*
import com.bestDate.db.entity.QuestionnaireDB
import com.bestDate.network.services.UserService
import javax.inject.Inject

class UserRemoteData @Inject constructor(
    private val service: UserService) {

    suspend fun getUserData() = service.getUserData()

    suspend fun getUserById(id: Int) = service.getUserById(id)

    suspend fun saveQuestionnaire(questionnaire: QuestionnaireDB) =
        service.saveQuestionnaire(questionnaire)

    suspend fun getUserLikes() = service.getLikesList()

    suspend fun getUsers(filters: FilterOptions, page: Int) =
        service.getUsers(filters, page)

    suspend fun getUserMatches() = service.getMatchesList()

    suspend fun getMyDuels() = service.getMyDuels()

    suspend fun blockUser(id: Int) = service.blockUser(id)

    suspend fun unlockUser(id: Int) = service.unlockUser(id)

    suspend fun changeLanguage(language: String) = service.changeLanguage(RequestLanguage(language))

    suspend fun getUserInvitations(filter: InvitationFilter) =
        service.getInvitations(UserInvitationRequest(filter.serverName)
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
}