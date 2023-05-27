package com.bestDate.presentation.passRecovery

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
import com.bestDate.data.utils.notifications.PusherCenter
import com.bestDate.presentation.auth.AuthUseCase
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.registration.RegistrationType
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PassRecoveryViewModel @Inject constructor(
    private val passRecoveryUseCase: RecoveryUseCase,
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase,
    private val pusherCenter: PusherCenter,
    private val preferencesUtils: PreferencesUtils
    ): BaseViewModel() {

    var user = userUseCase.getMyUser.asLiveData()

    private var _sendCodeLiveData = LiveEvent<Boolean>()
    val sendCodeLiveData: LiveData<Boolean> = _sendCodeLiveData

    private var _recoveryLiveData = LiveEvent<Boolean>()
    val recoveryLiveData: LiveData<Boolean> = _recoveryLiveData

    private var _validationErrorLiveData = MutableLiveData<Int>()
    val validationErrorLiveData: LiveData<Int> = _validationErrorLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun getSkipQuestionnaireCount() = preferencesUtils.getInt(Preferences.QUESTIONNAIRE_SKIP_COUNT)

    fun getSkipImageCount() = preferencesUtils.getInt(Preferences.IMAGE_SKIP_COUNT)

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

    fun confirmPassRecovery(code: String, appLanguage: String) {
        if (PassRecoveryDataHolder.type == RegistrationType.EMAIL) {
            confirmEmailPassRecovery(code, appLanguage)
        } else {
            confirmPhonePassRecovery(code, appLanguage)
        }
    }

    private fun confirmEmailPassRecovery(code: String, appLanguage: String) {
        _loadingLiveData.postValue(true)
        doAsync {
            passRecoveryUseCase.resetEmailPassword(
                PassRecoveryDataHolder.login,
                code,
                PassRecoveryDataHolder.password
            )
            loginByEmail(PassRecoveryDataHolder.login, PassRecoveryDataHolder.password, appLanguage)
        }
    }

    private fun confirmPhonePassRecovery(code: String, appLanguage: String) {
        _loadingLiveData.postValue(true)
        doAsync {
            passRecoveryUseCase.resetPhonePassword(
                PassRecoveryDataHolder.login,
                code,
                PassRecoveryDataHolder.password
            )
            loginByPhone(PassRecoveryDataHolder.login, PassRecoveryDataHolder.password, appLanguage)
        }
    }

    private fun loginByEmail(login: String, password: String, appLanguage: String) {
        doAsync {
            authUseCase.loginByEmail(login.trim(), password)
            userUseCase.changeLanguage(appLanguage)
            pusherCenter.startPusher()
            _recoveryLiveData.postValue(true)
            _loadingLiveData.postValue(false)
        }
    }

    private fun loginByPhone(login: String, password: String, appLanguage: String) {
        doAsync {
            authUseCase.loginByPhone(login.formatToPhoneNumber(), password)
            userUseCase.changeLanguage(appLanguage)
            pusherCenter.startPusher()
            _recoveryLiveData.postValue(true)
            _loadingLiveData.postValue(false)
        }
    }
}