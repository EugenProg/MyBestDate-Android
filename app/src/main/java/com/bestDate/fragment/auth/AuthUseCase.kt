package com.bestDate.fragment.auth

import com.bestDate.data.model.InternalException
import com.bestDate.network.remote.AuthRemoteData
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCase @Inject constructor(private val authRemoteData: AuthRemoteData) {

    suspend fun loginByEmail(email: String, password: String) {
        val response = authRemoteData.loginByEmail(email, password)
        if (response.isSuccessful) {

        } else {
            Timber.e(">>> ${response.message()}")
            throw InternalException.OperationException(response.message())
        }
    }
    suspend fun loginByPhone(phone: String, password: String) {
        val response = authRemoteData.loginByPhone(phone, password)
        if (response.isSuccessful) {
            Timber.e(">>> SUCCESS")
        } else {
            Timber.e(">>> ${response.message()}")
            throw InternalException.OperationException(response.message())
        }
    }

}