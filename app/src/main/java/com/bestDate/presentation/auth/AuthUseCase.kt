package com.bestDate.presentation.auth

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.AuthResponse
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.SocialProvider
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.network.remote.AuthRemoteData
import com.bestDate.network.remote.UserRemoteData
import com.bestDate.presentation.main.InvitationUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCase @Inject constructor(
    private val authRemoteData: AuthRemoteData,
    private val userRemoteData: UserRemoteData,
    private val invitationUseCase: InvitationUseCase,
    private val preferencesUtils: PreferencesUtils
) {
    var tokenIsFresh: Boolean = false
        private set

    suspend fun loginByEmail(email: String, password: String) {
        val response = authRemoteData.loginByEmail(email, password)
        if (response.isSuccessful) {
            saveTokens(response.body())
            saveDeviceToken()
            invitationUseCase.refreshInvitations()
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun loginByPhone(phone: String, password: String) {
        val response = authRemoteData.loginByPhone(phone, password)
        if (response.isSuccessful) {
            saveTokens(response.body())
            saveDeviceToken()
            invitationUseCase.refreshInvitations()
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun loginSocial(provider: SocialProvider, token: String?) {
        val response = authRemoteData.loginSocial(provider, token)
        if (response.isSuccessful) {
            saveTokens(response.body())
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    private suspend fun saveDeviceToken() {
        val token = preferencesUtils.getString(Preferences.FIREBASE_TOKEN)
        if (token.isNotBlank()) userRemoteData.saveMessagingDeviceToken(token)
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
            Preferences.ACCESS_TOKEN, "Bearer ${response?.access_token.orEmpty()}"
        )
        preferencesUtils.saveString(Preferences.REFRESH_TOKEN, response?.refresh_token.orEmpty())
        val expiresAt = System.currentTimeMillis() + (response?.expires_in.orZero)
        preferencesUtils.saveLong(Preferences.ARG_EXPIRES_AT, expiresAt - 3600)
        tokenIsFresh = true
    }
}