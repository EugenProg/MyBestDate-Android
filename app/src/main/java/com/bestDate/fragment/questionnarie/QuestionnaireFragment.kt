package com.bestDate.fragment.questionnarie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bestDate.R
import com.bestDate.base.BaseFragment
import com.bestDate.databinding.FragmentQuestionnaireBinding
import com.bestDate.view.questionnaire.QuestionnairePage
import com.bestDate.view.questionnaire.QuestionnairePageType

class QuestionnaireFragment : BaseFragment<FragmentQuestionnaireBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentQuestionnaireBinding =
        { inflater, parent, attach -> FragmentQuestionnaireBinding.inflate(inflater, parent, attach) }

    override val statusBarColor = R.color.bg_main

    override fun onInit() {
        super.onInit()

        binding.questionnaireView.setPages(getPages())
        binding.questionnaireView.progressAdded = {
            binding.progressBar.addProgress(it)
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

    private fun getPages(): MutableList<QuestionnairePage> {
        val pageList: MutableList<QuestionnairePage> = ArrayList()

        pageList.add(QuestionnairePage(
            number = 1,
            type = QuestionnairePageType.QUESTION_LIST,
            title = R.string.personal_information,
            questions = null
            )
        )

        pageList.add(QuestionnairePage(
            number = 2,
            type = QuestionnairePageType.QUESTION_LIST,
            title = R.string.your_appearance,
            questions = null
            )
        )

        pageList.add(QuestionnairePage(
            number = 3,
            type = QuestionnairePageType.QUESTION_LIST,
            title = R.string.search_condition,
            questions = null
            )
        )

        pageList.add(QuestionnairePage(
            number = 4,
            type = QuestionnairePageType.QUESTION_LIST,
            title = R.string.your_free_time,
            questions = null
            )
        )

        pageList.add(QuestionnairePage(
            number = 5,
            type = QuestionnairePageType.MULTILINE_INPUT,
            title = R.string.about_me,
            questions = null
            )
        )

        pageList.add(QuestionnairePage(
            number = 6,
            type = QuestionnairePageType.QUESTION_LIST,
            title = R.string.data_verification,
            questions = null,
            nextButtonText = R.string.well_done
            )
        )

        return pageList
    }

    override fun scrollAction() {
        super.scrollAction()
        binding.header.isVisible = false
    }

    override fun hideAction() {
        super.hideAction()
        binding.header.isVisible = true
    }
}