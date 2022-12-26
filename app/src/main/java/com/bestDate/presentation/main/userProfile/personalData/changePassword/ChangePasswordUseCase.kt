package com.bestDate.presentation.main.userProfile.personalData.changePassword

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChangePasswordUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData
) {

    suspend fun changePassword(oldPass: String, newPass: String) {
        val response = userRemoteData.changePassword(oldPass, newPass)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody().getErrorMessage()
        )
    }
}