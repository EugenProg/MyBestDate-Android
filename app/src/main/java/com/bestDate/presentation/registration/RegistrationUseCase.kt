package com.bestDate.presentation.registration

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.RegistrationRequest
import com.bestDate.network.remote.AuthRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationUseCase @Inject constructor(
    private val remoteData: AuthRemoteData
) {

    suspend fun sendEmailCode(email: String) {
        val response = remoteData.sendEmailCode(email)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun confirmEmailCode(email: String, code: String) {
        val response = remoteData.confirmEmailCode(email, code)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun createUserByEmail(registrationData: RegistrationRequest) {
        val response = remoteData.createUserByEmail(registrationData)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun sendPhoneCode(phone: String) {
        val response = remoteData.sendPhoneCode(phone)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun confirmPhoneCode(phone: String, code: String) {
        val response = remoteData.confirmPhoneCode(phone, code)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun createUserByPhone(registrationData: RegistrationRequest) {
        val response = remoteData.createUserByPhone(registrationData)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }
}