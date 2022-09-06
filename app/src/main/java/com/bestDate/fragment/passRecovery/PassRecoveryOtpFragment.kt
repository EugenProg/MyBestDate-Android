package com.bestDate.fragment.passRecovery

import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.base.BaseOtpFragment

class PassRecoveryOtpFragment: BaseOtpFragment(
    R.string.code_send_to_your_email,
    R.string.confirmation_code_sent_to_email_address,
    R.string.next) {

    private val args by navArgs<PassRecoveryOtpFragmentArgs>()

    override fun onInit() {
        super.onInit()
        setLogin(args.email)
    }

    override fun sendOtp(code: String) {
        navController.navigate(PassRecoveryOtpFragmentDirections
            .actionPassRecoveryOtpFragmentToPassRecoverySetNewFragment())
    }

}