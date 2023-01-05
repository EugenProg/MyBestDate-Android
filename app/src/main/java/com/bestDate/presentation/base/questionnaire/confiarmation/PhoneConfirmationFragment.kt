package com.bestDate.presentation.base.questionnaire.confiarmation

import com.bestDate.R
import com.bestDate.data.extension.isPhoneNumber
import com.bestDate.data.extension.observe
import com.bestDate.presentation.base.questionnaire.QuestionnaireQuestion
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhoneConfirmationFragment(
    question: QuestionnaireQuestion,
    isConfirmed: Boolean
) :
    BaseConfirmationFragment(question, isConfirmed) {
    override fun getInputHint(): Int {
        return R.string.phone_number
    }


    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.phoneCodeSuccessLiveData) {
            sendCodeSuccessful()
        }
        observe(viewModel.phoneSaveSuccessLiveData) {
            saveSuccessful()
        }
    }

    override fun save(input: String, code: String) {
        viewModel.saveUserPhone(input, code)
    }

    override fun validate(input: String) {
        when {
            input.isBlank() || !input.isPhoneNumber() -> setError()
            else -> viewModel.sendPhoneCode(input)
        }
    }
}