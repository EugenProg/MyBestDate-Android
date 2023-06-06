package com.bestDate.presentation.passRecovery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.databinding.FragmentPassRecoveryBinding
import com.bestDate.presentation.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PassRecoveryFragment : BaseVMFragment<FragmentPassRecoveryBinding, PassRecoveryViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPassRecoveryBinding =
        { inflater, parent, attach ->
            FragmentPassRecoveryBinding.inflate(
                inflater,
                parent,
                attach
            )
        }

    override val viewModelClass: Class<PassRecoveryViewModel> = PassRecoveryViewModel::class.java

    override val navBarColor = R.color.bg_main
    override val statusBarLight = true

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.backButton.onClick = {
            navController.popBackStack()
        }
        binding.recoveryButton.onSafeClick = {
            validate()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.sendCodeLiveData) {
            navController.navigate(
                PassRecoveryFragmentDirections
                    .actionPassRecoveryFragmentToPassRecoveryOtpFragment()
            )
        }
        observe(viewModel.loadingMode) {
            binding.recoveryButton.toggleActionEnabled(it)
        }
        observe(viewModel.validationErrorLiveData) {
            showMessage(it)
        }
        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
        }
    }

    private fun validate() {
        with(binding) {
            when {
                emailInput.text.isBlank() -> emailInput.setError()
                passInput.text.length < 6 -> passInput.setError()
                else -> {
                    PassRecoveryDataHolder.login = emailInput.text
                    PassRecoveryDataHolder.password = passInput.text
                    viewModel.sendPassRecoveryCode(emailInput.text)
                }
            }
        }
    }

    override fun scrollAction() {
        super.scrollAction()
        binding.scroll.fullScroll(View.FOCUS_DOWN)
        binding.emailInput.setFocus()
    }
}