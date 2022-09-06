package com.bestDate.fragment.registration

import com.bestDate.data.model.InternalException
import com.bestDate.data.model.RegistrationRequest
import com.bestDate.network.remote.AuthRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationUseCase @Inject constructor(
    private val remoteData: AuthRemoteData) {

    suspend fun sendEmailCode(email: String) {
        val request = remoteData.sendEmailCode(email)
        if (request.isSuccessful) {

        } else throw InternalException.OperationException(request.message())
    }

    suspend fun confirmEmailCode(email: String, code: String) {
        val request = remoteData.confirmEmailCode(email, code)
        if (request.isSuccessful) {

        } else throw InternalException.OperationException(request.message())
    }

    suspend fun createUserByEmail(registrationData: RegistrationRequest) {
        val request = remoteData.createUserByEmail(registrationData)
        if (request.isSuccessful) {

        } else throw InternalException.OperationException(request.message())
    }

    suspend fun sendPhoneCode(phone: String) {
        val request = remoteData.sendPhoneCode(phone)
        if (request.isSuccessful) {

        } else throw InternalException.OperationException(request.message())
    }

    suspend fun confirmPhoneCode(phone: String, code: String) {
        val request = remoteData.confirmPhoneCode(phone, code)
        if (request.isSuccessful) {

        } else throw InternalException.OperationException(request.message())
    }

    suspend fun createUserByPhone(registrationData: RegistrationRequest) {
        val request = remoteData.createUserByPhone(registrationData)
        if (request.isSuccessful) {

        } else throw InternalException.OperationException(request.message())
    }
}