package com.bestDate.presentation.auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.data.extension.setPaddingBottom
import com.bestDate.data.utils.ViewUtils
import com.bestDate.databinding.FragmentAuthBinding
import com.bestDate.presentation.base.BaseAuthFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : BaseAuthFragment<FragmentAuthBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAuthBinding =
        { inflater, parent, attach -> FragmentAuthBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<AuthViewModel> = AuthViewModel::class.java

    override val navBarColor = R.color.main_dark
    override val statusBarLight = true
    private var isLoggedIn = false

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.onClick = {
                navController.popBackStack()
            }
            authButton.onSafeClick = {
                validate()
            }
            socialContainer.googleClick = {
                loginByGoogle()
            }
            socialContainer.facebookClick = {
                //TODO: sign up with Facebook
                showMessage("facebook")
            }
            passForgotButton.setOnClickListener {
                navController.navigate(
                    AuthFragmentDirections
                        .actionAuthFragmentToPassRecoveryFragment()
                )
            }
            signUpButton.setOnClickListener {
                navController.navigate(
                    AuthFragmentDirections.actionAuthToStartRegistration()
                )
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.loadingMode.observe(viewLifecycleOwner) {
            binding.authButton.toggleActionEnabled(it)
        }
        viewModel.user.observe(viewLifecycleOwner) {
            if (viewModel.user.value != null && isLoggedIn) {
                val language = getString(R.string.app_locale)
                if (language != viewModel.user.value?.language) {
                    viewModel.changeLanguage(language)
                } else {
                    chooseRoute()
                }
            }
        }
        viewModel.validationErrorLiveData.observe(viewLifecycleOwner) {
            isLoggedIn = false
            showMessage(it)
        }
        viewModel.updateLanguageSuccessLiveData.observe(viewLifecycleOwner) {
            chooseRoute()
        }
    }

    private fun chooseRoute() {
        when {
            viewModel.user.value?.hasNoPhotos() == true -> {
                navController.navigate(AuthFragmentDirections.actionAuthToProfilePhotoEditing())
            }
            viewModel.user.value?.questionnaireEmpty() == true -> {
                navController.navigate(AuthFragmentDirections.actionAuthToQuestionnaire())
            }
            else -> {
                navController.navigate(AuthFragmentDirections.actionAuthToMain())
            }
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
                    isLoggedIn = true
                    viewModel.logIn(emailInput.text, passInput.text)
                }
            }
        }
    }
}