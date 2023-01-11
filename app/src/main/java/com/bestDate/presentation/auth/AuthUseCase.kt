package com.bestDate.presentation.auth

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.AuthResponse
import com.bestDate.data.model.InternalException
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.network.remote.AuthRemoteData
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCase @Inject constructor(
    private val authRemoteData: AuthRemoteData,
    private val preferencesUtils: PreferencesUtils
) {

    suspend fun loginByEmail(email: String, password: String) {
        val response = authRemoteData.loginByEmail(email, password)
        if (response.isSuccessful) {
            saveTokens(response.body())
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun loginByPhone(phone: String, password: String) {
        val response = authRemoteData.loginByPhone(phone, password)
        if (response.isSuccessful) {
            saveTokens(response.body())
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun refreshToken() {
        val response =
            authRemoteData.refreshToken(preferencesUtils.getString(Preferences.REFRESH_TOKEN))
        if (response.isSuccessful) {
            saveTokens(response.body())
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    private fun saveTokens(response: AuthResponse?) {
        preferencesUtils.saveString(
            Preferences.ACCESS_TOKEN,
            "Bearer ${response?.access_token.orEmpty()}"
        )
        preferencesUtils.saveString(Preferences.REFRESH_TOKEN, response?.refresh_token.orEmpty())
        val timeInSecs = Calendar.getInstance().timeInMillis
        val expiresAt = timeInSecs + (response?.expires_in ?: 0)
        preferencesUtils.saveLong(Preferences.ARG_EXPIRES_AT, expiresAt)
    }
}