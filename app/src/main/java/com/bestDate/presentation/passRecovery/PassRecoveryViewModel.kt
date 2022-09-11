package com.bestDate.presentation.passRecovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.R
import com.bestDate.base.BaseViewModel
import com.bestDate.data.extension.formatToPhoneNumber
import com.bestDate.data.extension.isAEmail
import com.bestDate.data.extension.isPhoneNumber
import com.bestDate.presentation.auth.AuthUseCase
import com.bestDate.presentation.registration.RegistrationType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PassRecoveryViewModel @Inject constructor(
    private val passRecoveryUseCase: RecoveryUseCase,
    private val authUseCase: AuthUseCase): BaseViewModel() {

    private var _sendCodeLiveData = MutableLiveData<Boolean>()
    val sendCodeLiveData: LiveData<Boolean> = _sendCodeLiveData

    private var _recoveryLiveData = MutableLiveData<Boolean>()
    val recoveryLiveData: LiveData<Boolean> = _recoveryLiveData

    private var _validationErrorLiveData = MutableLiveData<Int>()
    val validationErrorLiveData: LiveData<Int> = _validationErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun sendPassRecoveryCode(login: String) {
        when {
            login.isPhoneNumber() -> sendPhoneRecoveryCode(login)
            login.isAEmail() -> sendEmailRecoveryCode(login)
            else -> _validationErrorLiveData.value = R.string.enter_email_or_phone
        }
    }

    private fun sendEmailRecoveryCode(login: String) {
        PassRecoveryDataHolder.type = RegistrationType.EMAIL
        doAsync {
            passRecoveryUseCase.sendResetEmailCode(login)
            _sendCodeLiveData.postValue(true)
        }
    }

    private fun sendPhoneRecoveryCode(login: String) {
        PassRecoveryDataHolder.type = RegistrationType.PHONE
        doAsync {
            passRecoveryUseCase.sendResetPhoneCode(login)
            _sendCodeLiveData.postValue(true)
        }
    }

    fun confirmPassRecovery() {
        if (PassRecoveryDataHolder.type == RegistrationType.EMAIL) {
            confirmEmailPassRecovery()
        } else {
            confirmPhonePassRecovery()
        }
    }

    private fun confirmEmailPassRecovery() {
        _loadingLiveData.postValue(true)
        doAsync {
            passRecoveryUseCase.resetEmailPassword(
                PassRecoveryDataHolder.login,
                PassRecoveryDataHolder.code,
                PassRecoveryDataHolder.password
            )
            loginByEmail(PassRecoveryDataHolder.login, PassRecoveryDataHolder.password)
        }
    }

    private fun confirmPhonePassRecovery() {
        _loadingLiveData.postValue(true)
        doAsync {
            passRecoveryUseCase.resetPhonePassword(
                PassRecoveryDataHolder.login,
                PassRecoveryDataHolder.code,
                PassRecoveryDataHolder.password
            )
            loginByPhone(PassRecoveryDataHolder.login, PassRecoveryDataHolder.password)
        }
    }

    private fun loginByEmail(login: String, password: String) {
        doAsync {
            authUseCase.loginByEmail(login.trim(), password)
            _recoveryLiveData.postValue(true)
            _loadingLiveData.postValue(false)
        }
    }

    private fun loginByPhone(login: String, password: String) {
        doAsync {
            authUseCase.loginByPhone(login.formatToPhoneNumber(), password)
            _recoveryLiveData.postValue(true)
            _loadingLiveData.postValue(false)
        }
    }


}