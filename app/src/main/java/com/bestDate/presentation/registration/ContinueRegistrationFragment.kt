package com.bestDate.presentation.registration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.toStringFormat
import com.bestDate.databinding.FragmentContinueRegistrationBinding
import com.bestDate.presentation.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContinueRegistrationFragment :
    BaseVMFragment<FragmentContinueRegistrationBinding, RegistrationViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentContinueRegistrationBinding = { inflater, parent, attach ->
        FragmentContinueRegistrationBinding.inflate(inflater, parent, attach)
    }
    override val viewModelClass: Class<RegistrationViewModel> = RegistrationViewModel::class.java

    override val statusBarLight = true

    override fun onInit() {
        super.onInit()
        with(binding) {
            emailInput.text = RegistrationHolder.login
            passInput.text = RegistrationHolder.password

            name.text = RegistrationHolder.name
            birthdate.text = RegistrationHolder.birthdate?.toStringFormat()
            RegistrationHolder.gender?.line?.let { gender.text = getString(it) }
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.onClick = {
                navController.popBackStack()
            }
            signUpButton.onSafeClick = {
                validate()
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.sendCodeLiveData) {
            navController.navigate(
                ContinueRegistrationFragmentDirections
                    .actionContinueRegistrationFragmentToRegistrationOtpFragment()
            )
        }
        observe(viewModel.validationErrorLiveData) {
            showMessage(it)
        }
        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
        }
        observe(viewModel.loadingMode) {
            binding.signUpButton.toggleActionEnabled(it)
        }
    }

    private fun validate() {
        with(binding) {
            when {
                emailInput.text.isBlank() -> emailInput.setError()
                passInput.text.isBlank() || passInput.text.length < 6 -> passInput.setError()
                else -> {
                    RegistrationHolder.login = emailInput.text
                    RegistrationHolder.password = passInput.text
                    viewModel.sendRegistrationCode(emailInput.text)
                }
            }
        }
    }

    override fun scrollAction() {
        super.scrollAction()
        with(binding) {
            var focusView = emailInput
            if (passInput.hasFocus) focusView = passInput
            scroll.fullScroll(View.FOCUS_DOWN)
            focusView.setFocus()
        }
    }
}