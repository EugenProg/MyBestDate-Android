package com.bestDate.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.R
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.data.extension.formatToPhoneNumber
import com.bestDate.data.extension.isAEmail
import com.bestDate.data.extension.isPhoneNumber
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.presentation.main.UserUseCase
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase,
    private val preferencesUtils: PreferencesUtils
) : BaseViewModel() {

    private var _validationErrorLiveData = MutableLiveData<Int>()
    val validationErrorLiveData: LiveData<Int> = _validationErrorLiveData

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
        doAsync {
            authUseCase.loginByEmail(login.trim(), password)
            userUseCase.refreshUser()
            preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, false)
        }
    }

    private fun loginByPhone(login: String, password: String) {
        doAsync {
            authUseCase.loginByPhone(login.formatToPhoneNumber(), password)
            userUseCase.refreshUser()
            preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, false)
        }
    }

    fun changeLanguage(language: String) {
        doAsync {
            userUseCase.changeLanguage(language)
            _updateLanguageSuccessLiveData.postValue(true)
        }
    }
}