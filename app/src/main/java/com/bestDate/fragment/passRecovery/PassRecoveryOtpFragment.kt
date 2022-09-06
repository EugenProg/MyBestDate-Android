package com.bestDate.fragment.passRecovery

import com.bestDate.R
import com.bestDate.base.BaseOtpFragment

class PassRecoveryOtpFragment: BaseOtpFragment(
    R.string.code_send_to_your_email,
    R.string.confirmation_code_sent_to_email_address,
    R.string.next) {

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