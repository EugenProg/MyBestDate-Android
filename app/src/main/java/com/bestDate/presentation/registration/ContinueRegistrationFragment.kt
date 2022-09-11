package com.bestDate.presentation.registration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.toStringFormat
import com.bestDate.databinding.FragmentContinueRegistrationBinding
import com.bestDate.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContinueRegistrationFragment :
    BaseVMFragment<FragmentContinueRegistrationBinding, RegistrationViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentContinueRegistrationBinding = { inflater, parent, attach ->
        FragmentContinueRegistrationBinding.inflate(inflater, parent, attach)}
    override val viewModelClass: Class<RegistrationViewModel> = RegistrationViewModel::class.java

    override val statusBarLight = true

    override fun onInit() {
        super.onInit()
        with(binding) {
            emailInput.hint = getString(R.string.email_or_phone_number)
            emailInput.text = RegistrationHolder.login

            passInput.hint = getString(R.string.password)
            passInput.isPasswordField = true
            passInput.text = RegistrationHolder.password

            signUpButton.title = getString(R.string.sign_up)

            name.text = RegistrationHolder.name
            birthdate.text = RegistrationHolder.birthdate?.toStringFormat()
            RegistrationHolder.gender?.line?.let { gender.text = getString(it) }
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.setOnClickListener {
                navController.popBackStack()
            }
            signUpButton.onSafeClick = {
                validate()
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.sendCodeLiveData.observe(viewLifecycleOwner) {
            navController.navigate(ContinueRegistrationFragmentDirections
                .actionContinueRegistrationFragmentToRegistrationOtpFragment()
            )
        }
        viewModel.validationErrorLiveData.observe(viewLifecycleOwner) {
            showMessage(it)
        }
        viewModel.errorLive.observe(viewLifecycleOwner) {
            showMessage(it.exception.message)
            //showMessage(R.string.oops_its_error)
        }
        viewModel.loadingMode.observe(viewLifecycleOwner) {
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