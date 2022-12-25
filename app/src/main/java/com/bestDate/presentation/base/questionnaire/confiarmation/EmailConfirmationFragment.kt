package com.bestDate.presentation.base.questionnaire.confiarmation

import com.bestDate.R
import com.bestDate.presentation.base.questionnaire.QuestionnaireQuestion
import com.bestDate.data.extension.isAEmail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailConfirmationFragment(question: QuestionnaireQuestion, isConfirmed: Boolean) :
    BaseConfirmationFragment(question, isConfirmed) {

    override fun getInputHint(): Int {
        return R.string.enter_email
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.emailCodeSuccessLiveData.observe(viewLifecycleOwner) {
            sendCodeSuccessful()
        }
        viewModel.emailSaveSuccessLiveData.observe(viewLifecycleOwner) {
            saveSuccessful()
        }
    }

    override fun save(input: String, code: String) {
        viewModel.saveUserEmail(input, code)
    }

    override fun validate(input: String) {
        when {
            input.isBlank() || !input.isAEmail() -> setError()
            else -> viewModel.sendEmailCode(input)
        }
    }
}