package com.bestDate.fragment.registration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.toStringFormat
import com.bestDate.data.locale.RegistrationDataHolder
import com.bestDate.databinding.FragmentContinueRegistrationBinding
import com.bestDate.base.BaseFragment

class ContinueRegistrationFragment : BaseFragment<FragmentContinueRegistrationBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentContinueRegistrationBinding = { inflater, parent, attach ->
        FragmentContinueRegistrationBinding.inflate(inflater, parent, attach)}

    override val statusBarLight = true

    override fun onInit() {
        super.onInit()
        with(binding) {
            emailInput.hint = getString(R.string.email_or_phone_number)
            passInput.hint = getString(R.string.password)
            passInput.isPasswordField = true
            signUpButton.title = getString(R.string.sign_up)

            name.text = RegistrationDataHolder.username
            birthdate.text = RegistrationDataHolder.birthdate?.toStringFormat()
            gender.text = RegistrationDataHolder.gender
            emailInput.text = RegistrationDataHolder.email.orEmpty()
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

    private fun validate() {
        with(binding) {
            when {
                emailInput.text.isBlank() -> emailInput.setError()
                passInput.text.isBlank() || passInput.text.length < 6 -> passInput.setError()
                else -> {
                    RegistrationDataHolder.email = emailInput.text
                    RegistrationDataHolder.password = passInput.text
                    navController.navigate(ContinueRegistrationFragmentDirections
                        .actionContinueRegistrationFragmentToRegistrationOtpFragment(
                            RegistrationDataHolder.email.orEmpty()
                        )
                    )
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