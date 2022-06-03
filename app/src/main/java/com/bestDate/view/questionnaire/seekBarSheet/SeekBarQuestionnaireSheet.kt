package com.bestDate.view.questionnaire.seekBarSheet

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import com.bestDate.R
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.SheetSeekBarQuestionnaireBinding
import com.bestDate.view.base.BaseBottomSheet
import com.bestDate.view.questionnaire.list.QuestionnaireQuestion

class SeekBarQuestionnaireSheet: BaseBottomSheet<SheetSeekBarQuestionnaireBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetSeekBarQuestionnaireBinding =
        { inflater, parent, attach -> SheetSeekBarQuestionnaireBinding.inflate(inflater, parent, attach) }

    override fun getTheme() = R.style.WhiteBottomSheetDialogTheme
    private lateinit var question: QuestionnaireQuestion
    var onClose: ((progress: String) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        with(binding) {
            percentNumber.text = question.questionInfo?.percent.orZero.toString()
            title.text = getString(question.questionInfo?.question.orZero)

            bar.minProgress = getMinProgress()
            bar.maxProgress = getMaxProgress()
            bar.progress = getQuestionProgress()
        }
    }

    private fun getQuestionProgress(): Int {
        return if (question.answer == null) {
            val max = binding.bar.maxProgress
            val min = binding.bar.minProgress
            min + ((max - min) / 2)
        } else {
            if (question.answer.orEmpty().isDigitsOnly()) question.answer?.toInt().orZero else 0
        }
    }

    private fun getMaxProgress(): Int {
        val answers = question.questionInfo?.answers
        return if ((answers?.size ?: 0) > 3) {
            ((answers?.get(2) ?: 1) * 100) + (answers?.get(3) ?: 0) * 10
        } else {
            if ((answers?.size ?: 0) > 1) answers?.get(1) ?: 0 else 0
        }
    }

    private fun getMinProgress(): Int {
        val answers = question.questionInfo?.answers
        return if ((answers?.size ?: 0) > 3) {
            ((answers?.get(0) ?: 1) * 100) + (answers?.get(1) ?: 0) * 10
        } else {
            if ((answers?.size ?: 0) > 0) answers?.get(0) ?: 0 else 0
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.cancelButton.setOnSaveClickListener { this.dismiss() }
    }

    fun setInfo(question: QuestionnaireQuestion) {
        this.question = question
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onClose?.invoke(binding.bar.progress.toString())
    }

}