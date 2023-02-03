package com.bestDate.presentation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.R
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.data.extension.formatToPhoneNumber
import com.bestDate.data.extension.isAEmail
import com.bestDate.data.extension.isPhoneNumber
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.data.utils.notifications.PusherCenter
import com.bestDate.presentation.auth.AuthUseCase
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val registrationUseCase: RegistrationUseCase,
    private val userUseCase: UserUseCase,
    private val pusherCenter: PusherCenter,
    private val preferencesUtils: PreferencesUtils
    ) : BaseViewModel() {

    private var _sendCodeLiveData = MutableLiveData<Boolean>()
    val sendCodeLiveData: LiveData<Boolean> = _sendCodeLiveData

    private var _registrationLiveData = MutableLiveData<Boolean>()
    val registrationLiveData: LiveData<Boolean> = _registrationLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private var _validationErrorLiveData = MutableLiveData<Int>()
    val validationErrorLiveData: LiveData<Int> = _validationErrorLiveData

    fun sendRegistrationCode(login: String) {
        when {
            login.isPhoneNumber() -> sendPhoneCode(login)
            login.isAEmail() -> sendEmailCode(login)
            else -> _validationErrorLiveData.value = R.string.enter_email_or_phone
        }
    }

    private fun sendEmailCode(login: String) {
        RegistrationHolder.type = RegistrationType.EMAIL
        doAsync {
            registrationUseCase.sendEmailCode(login)
            _sendCodeLiveData.postValue(true)
        }
    }

    private fun sendPhoneCode(login: String) {
        RegistrationHolder.type = RegistrationType.PHONE
        doAsync {
            registrationUseCase.sendPhoneCode(login.formatToPhoneNumber())
            _sendCodeLiveData.postValue(true)
        }
    }

    fun confirmRegistration(code: String) {
        _loadingLiveData.value = true
        preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, true)
        if (RegistrationHolder.type == RegistrationType.PHONE) {
            confirmPhoneOtp(code)
        } else {
            confirmEmailOtp(code)
        }
    }

    private fun confirmEmailOtp(code: String) {
        doAsync {
            registrationUseCase.confirmEmailCode(RegistrationHolder.login, code)
            createAccountByEmail()
        }
    }

    private fun createAccountByEmail() {
        doAsync {
            registrationUseCase.createUserByEmail(RegistrationHolder.getRegistrationData())
            loginByEmail(RegistrationHolder.login, RegistrationHolder.password)
        }
    }

    private fun confirmPhoneOtp(code: String) {
        doAsync {
            registrationUseCase.confirmPhoneCode(RegistrationHolder.login, code)
            createAccountByPhone()
        }
    }

    private fun createAccountByPhone() {
        doAsync {
            registrationUseCase.createUserByPhone(RegistrationHolder.getRegistrationData())
            loginByPhone(RegistrationHolder.login, RegistrationHolder.password)
        }
    }

    private fun loginByEmail(login: String, password: String) {
        doAsync {
            authUseCase.loginByEmail(login.trim(), password)
            userUseCase.refreshUser()
            pusherCenter.startPusher()
            _registrationLiveData.postValue(true)
            _loadingLiveData.postValue(false)
        }
    }

    private fun loginByPhone(login: String, password: String) {
        doAsync {
            authUseCase.loginByPhone(login.formatToPhoneNumber(), password)
            userUseCase.refreshUser()
            pusherCenter.startPusher()
            _registrationLiveData.postValue(true)
            _loadingLiveData.postValue(false)
        }
    }
}