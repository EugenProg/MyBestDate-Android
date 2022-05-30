package com.bestDate.fragment.questionnarie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.base.BaseFragment
import com.bestDate.data.extension.orZero
import com.bestDate.databinding.FragmentQuestionnaireBinding
import com.bestDate.view.questionnaire.itemSelectSheet.OneLineQuestionnaireSheet
import com.bestDate.view.questionnaire.seekBarSheet.SeekBarQuestionnaireSheet

class QuestionnaireFragment : BaseFragment<FragmentQuestionnaireBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentQuestionnaireBinding =
        { inflater, parent, attach -> FragmentQuestionnaireBinding.inflate(inflater, parent, attach) }

    override val statusBarColor = R.color.bg_main

    private val oneLineSheet: OneLineQuestionnaireSheet = OneLineQuestionnaireSheet()
    private var seekBarSheet: SeekBarQuestionnaireSheet = SeekBarQuestionnaireSheet()

    override fun onInit() {
        super.onInit()

        binding.questionnaireView.viewLifecycle(viewLifecycleOwner)
        binding.questionnaireView.setPages(QuestionnaireItemsList().getPages())
        binding.questionnaireView.progressAdded = {
            binding.progressBar.addProgress(it)
        }
        binding.questionnaireView.questionClick = { question, list ->
            when (question.questionInfo?.viewType) {
                QuestionnaireViewType.ONE_LINE_INFO_VIEW -> {
                    oneLineSheet.setInfo(question)
                    oneLineSheet.show(childFragmentManager, oneLineSheet.tag)
                    oneLineSheet.itemClick = {
                        binding.questionnaireView.updateQuestionnaireList(question, it, list)
                        oneLineSheet.dismiss()
                    }
                }
                QuestionnaireViewType.SEEKBAR_VIEW -> {
                    seekBarSheet = SeekBarQuestionnaireSheet()
                    seekBarSheet.setInfo(question)
                    seekBarSheet.show(childFragmentManager, seekBarSheet.tag)
                    seekBarSheet.onClose = {
                        binding.questionnaireView.updateQuestionnaireList(question, it.toString(), list)
                    }
                }
                QuestionnaireViewType.RANGE_BAR_VIEW -> {

                }
            }
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.setOnClickListener {
                navController.popBackStack()
            }
            progressBar.onProgressChanged = {
                if (it > 46) centerSurprise.hideSurprise()
                else centerSurprise.showSurprise()

                if (it > 85) endSurprise.hideSurprise()
                else endSurprise.showSurprise()

                progressNumber.text = progressBar.progress.toString()
            }
            questionnaireView.collapseAction = {
                header.isVisible = !it
            }
            questionnaireView.keyboardHideAction = {
                hideKeyboardAction()
            }
        }
    }

    override fun scrollAction() {
        super.scrollAction()
        binding.header.isVisible = false
        binding.questionnaireView.setKeyboardAction(true)
    }

    override fun hideAction() {
        super.hideAction()
        binding.header.isVisible = true
        binding.questionnaireView.setKeyboardAction(false)
    }
}