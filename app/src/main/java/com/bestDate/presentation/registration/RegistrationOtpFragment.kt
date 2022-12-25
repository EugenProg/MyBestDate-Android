package com.bestDate.presentation.registration

import androidx.fragment.app.viewModels
import com.bestDate.R
import com.bestDate.presentation.base.BaseOtpFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationOtpFragment: BaseOtpFragment(
    R.string.confirmation_code,
    R.string.on_the_email_you_specified_we_send_the_confirmation_code,
    R.string.confirm) {
    private val viewModel by viewModels<RegistrationViewModel>()

    override fun onInit() {
        super.onInit()
        setLogin(RegistrationHolder.login)
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.registrationLiveData.observe(viewLifecycleOwner) {
            navController.navigate(RegistrationOtpFragmentDirections
                .actionGlobalProfilePhotoEditingFragment())
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            showMessage(it.exception.message)
            binding.confirmButton.toggleActionEnabled(false)
        }
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.confirmButton.toggleActionEnabled(it)
        }
    }

    override fun sendOtp(code: String) {
        viewModel.confirmRegistration(code)
    }
}