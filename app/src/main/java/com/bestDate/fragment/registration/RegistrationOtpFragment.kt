package com.bestDate.fragment.registration

import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.base.BaseOtpFragment

class RegistrationOtpFragment: BaseOtpFragment(
    R.string.confirmation_code,
    R.string.on_the_email_you_specified_we_send_the_confirmation_code,
    R.string.confirm) {

    private val args by navArgs<RegistrationOtpFragmentArgs>()

    override fun onInit() {
        super.onInit()
        setEmail(args.email)
    }

    override fun sendOtp(code: String) {
        navController.navigate(RegistrationOtpFragmentDirections
            .actionRegistrationOtpFragmentToProfilePhotoEditingFragment())
    }
}