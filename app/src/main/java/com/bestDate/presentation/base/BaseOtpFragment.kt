package com.bestDate.presentation.base

import android.os.CountDownTimer
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.utils.Logger
import com.bestDate.databinding.FragmentBaseOtpBinding

abstract class BaseOtpFragment : BaseFragment<FragmentBaseOtpBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBaseOtpBinding =
        { inflater, parent, attach -> FragmentBaseOtpBinding.inflate(inflater, parent, attach) }

    private lateinit var timer: CountDownTimer
    abstract fun getTitle(): Int
    abstract fun getText(): Int
    abstract fun resendCode()
    abstract fun sendOtp(code: String)
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

            startTimer()
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.onClick = { goBack() }
            confirmButton.onClick = { sendOtp(binding.otpInput.text) }
            resendButton.setOnSaveClickListener {
                startTimer()
                resendCode()
            }

            otpInput.onTextChangeListener = {
                if (it.length >= 6) sendOtp(it)
            }
        }
    }

    protected fun setLogin(login: String) {
        binding.headerEmail.text = login
    }


    override fun scrollAction() {
        super.scrollAction()
        binding.resendButton.isVisible = false
    }

    override fun hideAction() {
        super.hideAction()
        binding.resendButton.isVisible = true
    }

    private fun startTimer() {
        binding.resendTitle.isVisible = false
        binding.timeView.isVisible = true
        try {
            timer = object : CountDownTimer(60000, 1000) {
                override fun onTick(t: Long) {
                    val time = (t / 1000).toInt()
                    binding.timeView.text =
                        resources.getQuantityString(R.plurals.seconds, time, time)
                }

                override fun onFinish() {
                    binding.resendTitle.isVisible = true
                    binding.timeView.isVisible = false
                }

            }
        } catch (e: Exception) {
            Logger.print("timer exception: ${e.message}")
        }

        timer.start()
    }

    override var customBackNavigation: Boolean = true

    override fun goBack() {
        timer.cancel()
        super.goBack()
    }
}