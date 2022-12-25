package com.bestDate.presentation.passRecovery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.databinding.FragmentPassRecoveryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PassRecoveryFragment : BaseVMFragment<FragmentPassRecoveryBinding, PassRecoveryViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPassRecoveryBinding =
        { inflater, parent, attach -> FragmentPassRecoveryBinding.inflate(inflater, parent, attach) }

    override val viewModelClass: Class<PassRecoveryViewModel> = PassRecoveryViewModel::class.java

    override val navBarColor = R.color.bg_main
    override val statusBarLight = true

    override fun onInit() {
        super.onInit()
        with(binding) {
            emailInput.hint = getString(R.string.email_or_phone_number)
            recoveryButton.title = getString(R.string.next)
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.backButton.setOnClickListener {
            navController.popBackStack()
        }
        binding.recoveryButton.onSafeClick = {
            validate()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.sendCodeLiveData.observe(viewLifecycleOwner) {
            navController.navigate(PassRecoveryFragmentDirections
                .actionPassRecoveryFragmentToPassRecoveryOtpFragment())
        }
        viewModel.loadingMode.observe(viewLifecycleOwner) {
            binding.recoveryButton.toggleActionEnabled(it)
        }
        viewModel.validationErrorLiveData.observe(viewLifecycleOwner) {
            showMessage(it)
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            showMessage(it.exception.message)
        }
    }

    private fun validate() {
        with(binding) {
            when {
                emailInput.text.isBlank() -> emailInput.setError()
                else -> {
                    PassRecoveryDataHolder.login = emailInput.text
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