package com.bestDate.presentation.auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.extension.setPaddingBottom
import com.bestDate.data.utils.ViewUtils
import com.bestDate.databinding.FragmentAuthBinding
import com.bestDate.presentation.base.BaseAuthFragment
import com.bestDate.presentation.registration.GenderType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : BaseAuthFragment<FragmentAuthBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAuthBinding =
        { inflater, parent, attach -> FragmentAuthBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<AuthViewModel> = AuthViewModel::class.java

    override val navBarColor = R.color.main_dark
    override val statusBarLight = true

    override fun onViewClickListener() {
        super.onViewClickListener()
        makeStatusBarTransparent(binding.scroll)
        with(binding) {
            authButton.onSafeClick = {
                validate()
            }
            socialContainer.googleClick = {
                loginByGoogle()
            }
            socialContainer.facebookClick = {
                loginWithFacebook()
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
        observe(viewModel.loginProcessLiveData) {
            binding.authButton.toggleActionEnabled(it)
        }
        observe(viewModel.validationErrorLiveData) {
            isLoggedIn = false
            showMessage(it)
        }
    }

    override fun errorAction() {
        binding.authButton.toggleActionEnabled(false)
        super.errorAction()
    }

    override fun navigateToMain() {
        navController.navigate(AuthFragmentDirections.actionAuthToMain())
    }

    override fun navigateToFillData(name: String?, birthDate: String?, genderType: GenderType) {
        navController.navigate(
            AuthFragmentDirections
                .actionAuthToFillRegistrationData(name, birthDate, genderType))
    }

    override fun navigateToPhoto() {
        navController.navigate(AuthFragmentDirections.actionAuthToProfilePhotoEditing())
    }

    override fun navigateToQuestionnaire() {
        navController.navigate(AuthFragmentDirections.actionAuthToQuestionnaire())
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
                    viewModel.logIn(emailInput.text, passInput.text, getString(R.string.app_locale))
                }
            }
        }
    }

    override fun goBack() {
        if (navController.backQueue.count() !in 3..4) requireActivity().finish()
        else super.goBack()
    }
}