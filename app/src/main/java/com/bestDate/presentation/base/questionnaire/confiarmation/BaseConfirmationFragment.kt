package com.bestDate.presentation.base.questionnaire.confiarmation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.presentation.base.questionnaire.QuestionnaireQuestion
import com.bestDate.presentation.base.questionnaire.QuestionnaireViewModel
import com.bestDate.databinding.FragmentConfirmationBinding

abstract class BaseConfirmationFragment(
    private val question: QuestionnaireQuestion,
    private val isConfirmed: Boolean
) :
    BaseVMFragment<FragmentConfirmationBinding, QuestionnaireViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentConfirmationBinding =
        { inflater, parent, attach ->
            FragmentConfirmationBinding.inflate(
                inflater,
                parent,
                attach
            )
        }
    override val viewModelClass: Class<QuestionnaireViewModel> = QuestionnaireViewModel::class.java

    override val statusBarLight = true
    override val navBarLight = true
    override val navBarColor = R.color.white

    var backClick: (() -> Unit)? = null
    override var customBackNavigation = true

    override fun onInit() {
        super.onInit()
        with(binding) {
            title.text = question.questionInfo?.name
            fieldInput.setText(question.answer)
            fieldInput.isChecked(isConfirmed)
            fieldInput.setPlaceholder(getString(getInputHint()))

            saveButton.isVisible = !isConfirmed
        }
    }

    abstract fun getInputHint(): Int

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.onClick = {
                goBack()
            }
            saveButton.onClick = {
                validate(binding.fieldInput.getText())
            }
            otpInput.codeIsReady = {
                save(binding.fieldInput.getText(), it)
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            binding.saveButton.toggleActionEnabled(false)
            showMessage(it.exception.message)
        }
        viewModel.loadingMode.observe(viewLifecycleOwner) {
            binding.saveButton.toggleActionEnabled(it)
        }
    }

    abstract fun save(input: String, code: String)

    protected fun sendCodeSuccessful() {
        binding.otpInput.isVisible = true
        binding.saveButton.title = getString(R.string.confirm)
    }

    protected fun saveSuccessful() {
        binding.otpInput.isVisible = false
        binding.saveButton.isVisible = false
        binding.fieldInput.isChecked(true)
    }

    abstract fun validate(input: String)

    protected fun setError() {
        binding.fieldInput.setError()
    }

    override fun goBack() {
        backClick?.invoke()
    }

    override fun scrollAction() {
        super.scrollAction()
        if (binding.fieldInput.hasFocus()) binding.fieldInput.setFocus()
        if (binding.otpInput.hasFocus) binding.otpInput.setFocus()
    }
}