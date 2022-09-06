package com.bestDate.fragment.passRecovery

import com.bestDate.base.BaseViewModel
import com.bestDate.data.extension.isAEmail
import com.bestDate.data.extension.isPhoneNumber
import com.bestDate.fragment.auth.AuthUseCase
import com.bestDate.fragment.registration.RegistrationType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PassRecoveryViewModel @Inject constructor(
    private val passRecoveryUseCase: RecoveryUseCase,
    private val authUseCase: AuthUseCase): BaseViewModel() {



    fun sendPassRecoveryCode(login: String) {
        when {
            login.isPhoneNumber() -> sendPhoneRecoveryCode(login)
            login.isAEmail() -> sendEmailRecoveryCode(login)
            else -> {}
        }
    }

    private fun sendEmailRecoveryCode(login: String) {
        PassRecoveryDataHolder.type = RegistrationType.EMAIL
        doAsync {
            passRecoveryUseCase.sendResetEmailCode(login)

        }
    }

    private fun sendPhoneRecoveryCode(login: String) {
        PassRecoveryDataHolder.type = RegistrationType.PHONE
        doAsync {
            passRecoveryUseCase.sendResetPhoneCode(login)
        }
    }


}