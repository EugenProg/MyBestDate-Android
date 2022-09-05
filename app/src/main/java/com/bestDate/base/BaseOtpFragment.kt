package com.bestDate.base

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.FragmentBaseOtpBinding

abstract class BaseOtpFragment(
    @StringRes private val title: Int,
    @StringRes private val text: Int,
    @StringRes private val buttonText: Int): BaseFragment<FragmentBaseOtpBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBaseOtpBinding =
        { inflater, parent, attach -> FragmentBaseOtpBinding.inflate(inflater, parent, attach) }

    override val statusBarLight = true

    override fun onInit() {
        super.onInit()
        with(binding) {
            headerTitle.text = getString(title)
            headerText.text = getString(text)
            confirmButton.title = getString(buttonText)
            otpInput.hint = getString(R.string.enter_the_confirmation_code)
            otpInput.inputType = InputType.TYPE_CLASS_NUMBER

            otpInput.icon = null
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.setOnSaveClickListener { goBack() }
            confirmButton.onClick = { sendOtp(binding.otpInput.text) }
            resendButton.setOnSaveClickListener { resendCode() }

            otpInput.onTextChangeListener = {
                if (it.length >= 6) sendOtp(it)
            }
        }
    }

    protected fun setEmail(email: String) {
        binding.headerEmail.text = email
    }

    protected open fun goBack() {
        navController.popBackStack()
    }

    abstract fun sendOtp(code: String)

    protected open fun resendCode() { }

    override fun scrollAction() {
        super.scrollAction()
        binding.resendButton.isVisible = false
    }

    override fun hideAction() {
        super.hideAction()
        binding.resendButton.isVisible = true
    }
}