package com.bestDate.presentation.base.questionnaire

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.databinding.FragmentQuestionnaireBinding
import com.bestDate.db.entity.QuestionnaireDB
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.presentation.base.questionnaire.confiarmation.EmailConfirmationFragment
import com.bestDate.presentation.base.questionnaire.confiarmation.PhoneConfirmationFragment
import com.bestDate.presentation.base.questionnaire.confiarmation.SocialConfirmationFragment
import com.bestDate.presentation.base.questionnaire.search.SearchQuestionnaireLocationFragment
import com.bestDate.view.questionnaire.itemSelectSheet.MultilineQuestionnaireSheet
import com.bestDate.view.questionnaire.itemSelectSheet.OneLineQuestionnaireSheet
import com.bestDate.view.questionnaire.seekBarSheet.SeekBarQuestionnaireSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseQuestionnaireFragment :
    BaseVMFragment<FragmentQuestionnaireBinding, QuestionnaireViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentQuestionnaireBinding =
        { inflater, parent, attach ->
            FragmentQuestionnaireBinding.inflate(
                inflater,
                parent,
                attach
            )
        }
    override val viewModelClass: Class<QuestionnaireViewModel> = QuestionnaireViewModel::class.java

    override val statusBarColor = R.color.bg_main

    private var pagesLiveData = MutableLiveData<MutableList<QuestionnairePage>>()
    private var savedQuestionnaire: QuestionnaireDB? = null

    override var customBackNavigation = true

    abstract fun back()
    abstract fun forward()
    open var showSkipButton: Boolean = true

    override fun onInit() {
        super.onInit()
        hideKeyboardAction()

        with(binding.questionnaireView) {
            viewLifecycle(viewLifecycleOwner)
            progressAdded = {
                binding.progressBar.addProgress(it)
            }
        }

        binding.skipButton.isVisible = showSkipButton

        lifecycleScope.launchWhenResumed {
            postDelayed({
                pagesLiveData.postValue(QuestionnaireItemsList().getPages())
            }, 310)
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.onClick = {
                back()
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

            questionnaireView.questionClick = questionnaireClickAction()
            questionnaireView.finishClick = {
                val questionnaire = binding.questionnaireView.getQuestionnaire()
                if (questionnaireIsUpdate(questionnaire)) {
                    viewModel.saveQuestionnaire(questionnaire)
                } else forward()
            }
            skipButton.setOnSaveClickListener {
                forward()
            }
        }
    }

    private fun questionnaireIsUpdate(questionnaire: QuestionnaireDB): Boolean {
        return !(questionnaire.purpose == savedQuestionnaire?.purpose &&
                questionnaire.expectations == savedQuestionnaire?.expectations &&
                questionnaire.height == savedQuestionnaire?.height &&
                questionnaire.weight == savedQuestionnaire?.weight &&
                questionnaire.eye_color == savedQuestionnaire?.eye_color &&
                questionnaire.hair_color == savedQuestionnaire?.hair_color &&
                questionnaire.hair_length == savedQuestionnaire?.hair_length &&
                questionnaire.marital_status == savedQuestionnaire?.marital_status &&
                questionnaire.kids == savedQuestionnaire?.kids &&
                questionnaire.education == savedQuestionnaire?.education &&
                questionnaire.occupation == savedQuestionnaire?.occupation &&
                questionnaire.about_me == savedQuestionnaire?.about_me &&
                questionnaire.search_age_min == savedQuestionnaire?.search_age_min &&
                questionnaire.search_age_max == savedQuestionnaire?.search_age_max &&
                questionnaire.search_country == savedQuestionnaire?.search_country &&
                questionnaire.search_city == savedQuestionnaire?.search_city &&
                questionnaire.hobby?.equals(savedQuestionnaire?.hobby) == true &&
                questionnaire.socials?.equals(savedQuestionnaire?.socials) == true &&
                questionnaire.sport?.equals(savedQuestionnaire?.sport) == true &&
                questionnaire.evening_time == savedQuestionnaire?.evening_time
                )
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(pagesLiveData) {
            binding.questionnaireView.setPages(it)
        }
        observe(viewModel.user) {
            savedQuestionnaire = it?.questionnaire
            binding.questionnaireView.setQuestionnaire(
                it?.questionnaire,
                it?.email, it?.phone, it?.photos?.size.orZero
            )
        }
        observe(viewModel.loadingMode) {
            binding.questionnaireView.toggleFinishButton(it)
        }
        observe(viewModel.questionnaireSaveLiveData) {
            forward()
        }
        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
        }
    }

    private fun questionnaireClickAction():
                (QuestionnaireQuestion, MutableLiveData<MutableList<QuestionnaireQuestion>>) -> Unit {
        return { question, list ->
            when (question.questionInfo?.viewType) {
                QuestionnaireViewType.ONE_LINE_INFO_VIEW -> {
                    val oneLineSheet = OneLineQuestionnaireSheet()
                    oneLineSheet.setInfo(question)
                    oneLineSheet.show(childFragmentManager, oneLineSheet.tag)
                    oneLineSheet.itemClick = {
                        binding.questionnaireView.updateQuestionnaireList(question, it, list)
                        oneLineSheet.dismiss()
                    }
                }
                QuestionnaireViewType.SEEKBAR_VIEW -> {
                    val seekBarSheet = SeekBarQuestionnaireSheet()
                    seekBarSheet.setInfo(question)
                    seekBarSheet.show(childFragmentManager, seekBarSheet.tag)
                    seekBarSheet.onClose = {
                        binding.questionnaireView.updateQuestionnaireList(question, it, list)
                    }
                }
                QuestionnaireViewType.CONFIRMATION_EMAIL -> {
                    val emailFragment = EmailConfirmationFragment(
                        question,
                        viewModel.user.value?.email_verification == true
                    )
                    openPage(emailFragment)

                    emailFragment.backClick = { closePage(emailFragment) }
                }
                QuestionnaireViewType.CONFIRMATION_PHONE -> {
                    val phoneFragment = PhoneConfirmationFragment(
                        question,
                        viewModel.user.value?.phone_verification == true
                    )
                    openPage(phoneFragment)

                    phoneFragment.backClick = { closePage(phoneFragment) }
                }
                QuestionnaireViewType.CONFIRMATION_SOCIAL -> {
                    val socialFragment = SocialConfirmationFragment(question)
                    openPage(socialFragment)

                    socialFragment.saveClick = {
                        closePage(socialFragment)
                        binding.questionnaireView.updateQuestionnaireList(question, it, list)
                    }

                    socialFragment.backClick = { closePage(socialFragment) }
                }
                QuestionnaireViewType.INPUT_LOCATION_VIEW -> {
                    val searchLocationFragment = SearchQuestionnaireLocationFragment(question)
                    openPage(searchLocationFragment)

                    searchLocationFragment.saveClick = {
                        closePage(searchLocationFragment)
                        binding.questionnaireView.updateQuestionnaireList(question, it, list)
                    }

                    searchLocationFragment.backClick = { closePage(searchLocationFragment) }
                }
                QuestionnaireViewType.MULTILINE_INFO_VIEW -> {
                    val multiLineSheet = MultilineQuestionnaireSheet()
                    multiLineSheet.setInfo(question)
                    multiLineSheet.show(childFragmentManager, multiLineSheet.tag)
                    multiLineSheet.onSave = {
                        binding.questionnaireView.updateQuestionnaireList(question, it, list)
                        multiLineSheet.dismiss()
                    }
                }
            }
        }
    }

    private fun openPage(fragment: Fragment) {
        open(fragment, binding.container)
    }

    private fun closePage(fragment: Fragment) {
        close(fragment, binding.container) {
            reDrawBars()
        }
    }

    override fun onCustomBackNavigation() {
        if (!binding.questionnaireView.goBack()) back()
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