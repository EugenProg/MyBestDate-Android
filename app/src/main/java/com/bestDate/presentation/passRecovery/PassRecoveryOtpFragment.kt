package com.bestDate.presentation.passRecovery

import androidx.fragment.app.viewModels
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.presentation.base.BaseOtpFragment
import com.bestDate.presentation.registration.RegistrationType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PassRecoveryOtpFragment : BaseOtpFragment() {
    private val viewModel by viewModels<PassRecoveryViewModel>()

    override fun getTitle(): Int =
        if (PassRecoveryDataHolder.type == RegistrationType.EMAIL) R.string.code_send_to_your_email
        else R.string.code_send_to_your_phone

    override fun getText(): Int =
        if (PassRecoveryDataHolder.type == RegistrationType.EMAIL) R.string.confirmation_code_sent_to_email_address
        else R.string.confirmation_code_sent_to_phone

    override fun resendCode() {
        viewModel.sendPassRecoveryCode(PassRecoveryDataHolder.login)
    }

    override fun getButtonTitle(): Int = R.string.confirm

    override fun onInit() {
        super.onInit()
        setLogin(PassRecoveryDataHolder.login)
    }

    override fun sendOtp(code: String) {
        viewModel.confirmPassRecovery(code, getString(R.string.app_locale))
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.recoveryLiveData) {
            chooseRoute()
        }
        observe(viewModel.loadingLiveData) {
            binding.confirmButton.toggleActionEnabled(it)
        }
        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
            binding.confirmButton.toggleActionEnabled(false)
        }
    }

    private fun chooseRoute() {
        when {
            viewModel.user.value?.hasNoPhotos() == true && viewModel.getSkipImageCount() < 3 -> {
                navController.navigate(
                    PassRecoverySetNewFragmentDirections.actionGlobalProfilePhotoEditingFragment()
                )
            }

            viewModel.user.value?.questionnaireEmpty() == true &&
                    viewModel.getSkipQuestionnaireCount() < 3 -> {
                navController.navigate(
                    PassRecoverySetNewFragmentDirections.actionGlobalQuestionnaireFragment()
                )
            }

            else -> {
                navController.navigate(
                    PassRecoverySetNewFragmentDirections.actionGlobalMainNavGraph()
                )
            }
        }
    }
}