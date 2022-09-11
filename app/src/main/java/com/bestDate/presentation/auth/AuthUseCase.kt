package com.bestDate.presentation.auth

import com.bestDate.data.model.AuthResponse
import com.bestDate.data.model.InternalException
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.network.remote.AuthRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCase @Inject constructor(
    private val authRemoteData: AuthRemoteData,
    private val preferencesUtils: PreferencesUtils) {

    suspend fun loginByEmail(email: String, password: String) {
        val response = authRemoteData.loginByEmail(email, password)
        if (response.isSuccessful) {
            saveTokens(response.body())
        } else throw InternalException.OperationException(response.message())
    }

    suspend fun loginByPhone(phone: String, password: String) {
        val response = authRemoteData.loginByPhone(phone, password)
        if (response.isSuccessful) {
            saveTokens(response.body())
        } else throw InternalException.OperationException(response.message())
    }

    private fun saveTokens(response: AuthResponse?) {
        preferencesUtils.saveString(Preferences.ACCESS_TOKEN, "Bearer ${response?.access_token.orEmpty()}")
        preferencesUtils.saveString(Preferences.REFRESH_TOKEN, response?.refresh_token.orEmpty())
    }
}