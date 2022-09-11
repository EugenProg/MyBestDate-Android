package com.bestDate.presentation.auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.data.extension.setPaddingBottom
import com.bestDate.data.utils.ViewUtils
import com.bestDate.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : BaseVMFragment<FragmentAuthBinding, AuthViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAuthBinding =
        { inflater, parent, attach -> FragmentAuthBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<AuthViewModel> = AuthViewModel::class.java

    override val navBarColor = R.color.main_dark
    override val statusBarLight = true

    override fun onInit() {
        super.onInit()
        with(binding) {
            emailInput.hint = getString(R.string.email_or_phone_number)
            passInput.hint = getString(R.string.password)
            passInput.isPasswordField = true
            authButton.title = getString(R.string.login)
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.setOnClickListener {
                navController.popBackStack()
            }
            authButton.onSafeClick = {
                validate()
            }
            socialContainer.googleClick = {
                //TODO: sign up with Google
                showMessage("google")
            }
            socialContainer.facebookClick = {
                //TODO: sign up with Facebook
                showMessage("facebook")
            }
            passForgotButton.setOnClickListener {
                navController.navigate(AuthFragmentDirections
                    .actionAuthFragmentToPassRecoveryFragment())
            }
            signUpButton.setOnClickListener {
                navController.navigate(AuthFragmentDirections
                    .actionAuthFragmentToStartRegistrationFragment())
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.loadingMode.observe(viewLifecycleOwner) {
            binding.authButton.toggleActionEnabled(it)
        }
        viewModel.loginLiveData.observe(viewLifecycleOwner) {
            navController.navigate(AuthFragmentDirections
                .actionAuthFragmentToProfilePhotoEditingFragment()
            )
        }
        viewModel.errorLive.observe(viewLifecycleOwner) {
            showMessage(getString(R.string.wrong_auth_data))
        }
        viewModel.validationErrorLiveData.observe(viewLifecycleOwner) {
            showMessage(it)
        }
    }

    override fun scrollAction() {
        super.scrollAction()
        with(binding) {
            ViewUtils().hideViews(socialContainer, signUpButton)
            inputContainer.setPaddingBottom(0)
            var focusView = emailInput
            if (passInput.hasFocus) focusView = passInput
            scroll.fullScroll(View.FOCUS_DOWN)
            focusView.setFocus()
        }
    }

    override fun hideAction() {
        super.hideAction()
        with(binding) {
            ViewUtils().showViews(socialContainer, signUpButton)
            inputContainer.setPaddingBottom(90)
        }
    }

    private fun validate() {
        with(binding) {
            when {
                emailInput.text.isBlank() -> emailInput.setError()
                passInput.text.isBlank() || passInput.text.length < 6 -> passInput.setError()
                else -> {
                    viewModel.logIn(emailInput.text, passInput.text)
                }
            }
        }

    }
}