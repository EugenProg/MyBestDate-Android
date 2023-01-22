package com.bestDate.presentation.passRecovery

import com.bestDate.R
import com.bestDate.presentation.base.BaseOtpFragment
import com.bestDate.presentation.registration.RegistrationType

class PassRecoveryOtpFragment: BaseOtpFragment() {
    override fun getTitle(): Int =
        if (PassRecoveryDataHolder.type == RegistrationType.EMAIL) R.string.code_send_to_your_email
        else R.string.code_send_to_your_phone

    override fun getText(): Int =
        if (PassRecoveryDataHolder.type == RegistrationType.EMAIL) R.string.confirmation_code_sent_to_email_address
        else R.string.confirmation_code_sent_to_phone

    override fun getButtonTitle(): Int = R.string.next

    override fun onInit() {
        super.onInit()
        setLogin(PassRecoveryDataHolder.login)
    }

    override fun sendOtp(code: String) {
        PassRecoveryDataHolder.code = code
        navController.navigate(PassRecoveryOtpFragmentDirections
            .actionPassRecoveryOtpFragmentToPassRecoverySetNewFragment())
    }

}