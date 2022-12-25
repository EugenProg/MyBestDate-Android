package com.bestDate.base.questionnaire.confiarmation

import com.bestDate.R
import com.bestDate.base.questionnaire.QuestionnaireQuestion
import com.bestDate.data.extension.isPhoneNumber
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
        viewModel.phoneCodeSuccessLiveData.observe(viewLifecycleOwner) {
            sendCodeSuccessful()
        }
        viewModel.phoneSaveSuccessLiveData.observe(viewLifecycleOwner) {
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