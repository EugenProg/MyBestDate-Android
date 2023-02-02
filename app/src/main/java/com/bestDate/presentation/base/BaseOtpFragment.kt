package com.bestDate.presentation.base

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.FragmentBaseOtpBinding

abstract class BaseOtpFragment : BaseFragment<FragmentBaseOtpBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBaseOtpBinding =
        { inflater, parent, attach -> FragmentBaseOtpBinding.inflate(inflater, parent, attach) }

    abstract fun getTitle(): Int
    abstract fun getText(): Int
    abstract fun getButtonTitle(): Int
    override val statusBarLight = true

    override fun onInit() {
        super.onInit()
        with(binding) {
            headerTitle.text = getString(getTitle())
            headerText.text = getString(getText())
            confirmButton.title = getString(getButtonTitle())
            otpInput.inputType = InputType.TYPE_CLASS_NUMBER

            otpInput.icon = null
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.onClick = { goBack() }
            confirmButton.onClick = { sendOtp(binding.otpInput.text) }
            resendButton.setOnSaveClickListener { resendCode() }

            otpInput.onTextChangeListener = {
                if (it.length >= 6) sendOtp(it)
            }
        }
    }

    protected fun setLogin(login: String) {
        binding.headerEmail.text = login
    }

    abstract fun sendOtp(code: String)

    protected open fun resendCode() {}

    override fun scrollAction() {
        super.scrollAction()
        binding.resendButton.isVisible = false
    }

    override fun hideAction() {
        super.hideAction()
        binding.resendButton.isVisible = true
    }
}