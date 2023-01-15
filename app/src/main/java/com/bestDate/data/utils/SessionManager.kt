package com.bestDate.data.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.data.model.AuthResponse
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.network.remote.AuthRemoteData
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Provider

class SessionManager @Inject constructor(
    private val preferencesUtils: PreferencesUtils,
    private val authRemoteDataProvider: Provider<AuthRemoteData>
) {
    private val _loggedOut = MutableLiveData(false)
    val loggedOut: LiveData<Boolean> = _loggedOut

    var accessToken: String = ""
        get() {
            return field.ifEmpty {
                preferencesUtils.getString(Preferences.ACCESS_TOKEN)
            }
        }
        set(value) {
            field = "Bearer $value"
            _loggedOut.postValue(false)
            preferencesUtils.saveString(Preferences.ACCESS_TOKEN, "Bearer $value")
        }

    var refreshToken: String
        get() = preferencesUtils.getString(Preferences.REFRESH_TOKEN)
        set(value) = preferencesUtils.saveString(Preferences.REFRESH_TOKEN, value)

    var expiresAt: Long
        get() = preferencesUtils.getLong(Preferences.ARG_EXPIRES_AT)
        set(value) = preferencesUtils.saveLong(Preferences.ARG_EXPIRES_AT, value - 3600)

    fun isAccessTokenExpired(): Boolean {
        return if (isAccessTokenEmpty()) true
        else System.currentTimeMillis() > expiresAt
    }

    private fun isAccessTokenEmpty(): Boolean =
        preferencesUtils.getString(Preferences.ACCESS_TOKEN).isBlank()

    fun refreshToken(): Response<AuthResponse> {
        val block = runBlocking {
            val response = authRemoteDataProvider.get().refreshToken(refreshToken)
            if (response.isSuccessful) {
                response.body()?.let {
                    accessToken = it.access_token.orEmpty()
                    refreshToken = it.refresh_token.orEmpty()
                }
                updateSession()
            }
            response
        }
        return block
    }


    private fun updateSession(accessToken: String? = null, refreshToken: String? = null) {
        if (accessToken != null && refreshToken != null) {
            this.accessToken = accessToken
            this.refreshToken = refreshToken
        }
        expiresAt = System.currentTimeMillis() + preferencesUtils.getLong(Preferences.ARG_EXPIRES_AT)
    }

    fun clearSession() {
        _loggedOut.postValue(true)
    }
}