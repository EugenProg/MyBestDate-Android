package com.bestDate.presentation.auth

import androidx.lifecycle.asLiveData
import com.bestDate.R
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.data.extension.formatToPhoneNumber
import com.bestDate.data.extension.isAEmail
import com.bestDate.data.extension.isPhoneNumber
import com.bestDate.data.model.SocialProvider
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.data.utils.notifications.PusherCenter
import com.bestDate.presentation.main.UserUseCase
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase,
    private val pusherCenter: PusherCenter,
    private val preferencesUtils: PreferencesUtils
) : BaseViewModel() {

    var registrationSocialMode: Boolean
    get() = authUseCase.registrationSocialMode
    set(value) {
        authUseCase.registrationSocialMode = value
    }

    private var _validationErrorLiveData = LiveEvent<Int>()
    val validationErrorLiveData: LiveEvent<Int> = _validationErrorLiveData

    private var _loginProcessLiveData = LiveEvent<Boolean>()
    val loginProcessLiveData: LiveEvent<Boolean> = _loginProcessLiveData

    var user = userUseCase.getMyUser.asLiveData()

    private var _updateLanguageSuccessLiveData: LiveEvent<Boolean> = LiveEvent()
    var updateLanguageSuccessLiveData: LiveEvent<Boolean> = _updateLanguageSuccessLiveData

    fun logIn(login: String, password: String) {
        when {
            login.isPhoneNumber() -> loginByPhone(login, password)
            login.isAEmail() -> loginByEmail(login, password)
            else -> _validationErrorLiveData.value = R.string.enter_email_or_phone
        }
    }

    private fun loginByEmail(login: String, password: String) {
        _loginProcessLiveData.value = true
        doAsync {
            authUseCase.loginByEmail(login.trim(), password)
            userUseCase.refreshUser()
            pusherCenter.startPusher()
            preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, false)
            _loginProcessLiveData.postValue(false)
        }
    }

    private fun loginByPhone(login: String, password: String) {
        _loginProcessLiveData.value = true
        doAsync {
            authUseCase.loginByPhone(login.formatToPhoneNumber(), password)
            userUseCase.refreshUser()
            pusherCenter.startPusher()
            preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, false)
            _loginProcessLiveData.postValue(false)
        }
    }

    fun loginSocial(provider: SocialProvider, token: String?) {
        doAsync {
            authUseCase.loginSocial(provider, token)
            userUseCase.refreshUser()
            pusherCenter.startPusher()
            preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, registrationSocialMode)
        }
    }

    fun loginByGoogle(authCode: String?) {
        doAsync {
            authUseCase.loginWithGoogle(authCode)
            userUseCase.refreshUser()
            pusherCenter.startPusher()
            preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, registrationSocialMode)
        }
    }

    fun changeLanguage(language: String) {
        doAsync {
            userUseCase.changeLanguage(language)
            _updateLanguageSuccessLiveData.postValue(true)
        }
    }
}