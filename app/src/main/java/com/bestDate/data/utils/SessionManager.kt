package com.bestDate.data.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.data.model.AuthResponse
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.network.remote.AuthRemoteData
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import timber.log.Timber
import java.util.*
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
            field = value
            _loggedOut.postValue(false)
            preferencesUtils.saveString(Preferences.ACCESS_TOKEN, value)
        }

    var refreshToken: String
        get() = preferencesUtils.getString(Preferences.REFRESH_TOKEN)
        set(value) = preferencesUtils.saveString(Preferences.REFRESH_TOKEN, value)

    var expiresAt: Long
        get() = preferencesUtils.getLong(Preferences.ARG_EXPIRES_AT)
        set(value) = preferencesUtils.saveLong(Preferences.ARG_EXPIRES_AT, value)

    fun isAccessTokenExpired(): Boolean {
        return if (isAccessTokenEmpty()) {
            true
        } else System.currentTimeMillis() > expiresAt
    }

    fun isAccessTokenEmpty(): Boolean =
        preferencesUtils.getString(Preferences.ACCESS_TOKEN).isNotBlank()

    fun refreshToken(): Response<AuthResponse> {
        val block = runBlocking {
            val response =
                authRemoteDataProvider.get().refreshToken(preferencesUtils.getString(Preferences.REFRESH_TOKEN))
            if (response.isSuccessful) {
                accessToken = response.body()?.access_token.orEmpty()
                updateSession()
            }
            response
        }
        return block
    }


    fun updateSession(accessToken: String? = null, refreshToken: String? = null) {
        if (accessToken != null && refreshToken != null) {
            this.accessToken = accessToken
            this.refreshToken = refreshToken
        }
        val timeInSecs = Calendar.getInstance().timeInMillis
        expiresAt = timeInSecs + 2000
                //preferencesUtils.getLong(Preferences.ARG_EXPIRES_AT)
        Timber.d("HELLO expiresAt %s", expiresAt)
        Timber.d("HELLO now %s", timeInSecs)
    }

    fun clearSession() {
        accessToken = ""
        refreshToken = ""
        expiresAt = 0L
        _loggedOut.postValue(true)
    }
}