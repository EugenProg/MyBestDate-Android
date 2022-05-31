package com.bestDate.fragment.questionnarie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import com.bestDate.R
import com.bestDate.base.BaseFragment
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.postDelayed
import com.bestDate.databinding.FragmentQuestionnaireBinding
import com.bestDate.view.questionnaire.itemSelectSheet.OneLineQuestionnaireSheet
import com.bestDate.view.questionnaire.list.QuestionnaireQuestion
import com.bestDate.view.questionnaire.seekBarSheet.SeekBarQuestionnaireSheet

class QuestionnaireFragment : BaseFragment<FragmentQuestionnaireBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentQuestionnaireBinding =
        { inflater, parent, attach -> FragmentQuestionnaireBinding.inflate(inflater, parent, attach) }

    override val statusBarColor = R.color.bg_main

    private val oneLineSheet: OneLineQuestionnaireSheet = OneLineQuestionnaireSheet()
    private var seekBarSheet: SeekBarQuestionnaireSheet = SeekBarQuestionnaireSheet()
    private var searchLocationFragment: SearchQuestionnaireLocationFragment? = null

    override var customBackNavigation = true

    override fun onInit() {
        super.onInit()

        binding.questionnaireView.viewLifecycle(viewLifecycleOwner)
        binding.questionnaireView.setPages(QuestionnaireItemsList().getPages())
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

            binding.questionnaireView.questionClick = questionnaireClickAction()

        }
    }

    private fun questionnaireClickAction():
                (QuestionnaireQuestion, MutableLiveData<MutableList<QuestionnaireQuestion>>) -> Unit {
        return { question, list ->
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
                        binding.questionnaireView.updateQuestionnaireList(
                            question,
                            it.toString(),
                            list
                        )
                    }
                }
                QuestionnaireViewType.CONFIRMATION_VIEW -> {
                    // showMessage(question.questionInfo?.question.orZero)
                }
                QuestionnaireViewType.INPUT_LOCATION_VIEW -> {
                    searchLocationFragment = SearchQuestionnaireLocationFragment(question)
                    childFragmentManager.commit {
                        setCustomAnimations(R.anim.push_up_in, R.anim.push_up_out)
                        searchLocationFragment?.let { replace(R.id.container, it) }
                    }

                    searchLocationFragment?.saveClick = {
                        closeSearchPage()
                        binding.questionnaireView.updateQuestionnaireList(question, it, list)
                    }

                    searchLocationFragment?.backClick = { closeSearchPage() }
                }
            }
        }
    }

    private fun closeSearchPage() {
        binding.container.animate()
            .translationY((binding.container.height).toFloat())
            .setDuration(300)
            .start()

        postDelayed({
            childFragmentManager.commit {
                searchLocationFragment?.let {
                    remove(it)
                    binding.container.isVisible = false
                    reDrawBars()
                    reDrawSearchPage()
                }
            }
        }, 350)
    }

    private fun reDrawSearchPage() {
        binding.container.animate()
            .translationY(0.0f)
            .setDuration(10)
            .start()

        postDelayed({
            binding.container.isVisible = true
        }, 20)
    }

    override fun onCustomBackNavigation() {
        super.onCustomBackNavigation()

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