package com.bestDate.presentation.passRecovery

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.network.remote.AuthRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecoveryUseCase @Inject constructor(private val authRemoteData: AuthRemoteData) {

    suspend fun sendResetEmailCode(email: String) {
        val response = authRemoteData.sendEmailResetCode(email)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun sendResetPhoneCode(phone: String) {
        val response = authRemoteData.sendPhoneResetCode(phone)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun resetEmailPassword(email: String, code: String, password: String) {
        val response = authRemoteData.resetEmailPassword(email, code, password)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun resetPhonePassword(phone: String, code: String, password: String) {
        val response = authRemoteData.resetPhonePassword(phone, code, password)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }
}