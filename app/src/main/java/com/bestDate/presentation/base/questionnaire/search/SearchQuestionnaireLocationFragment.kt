package com.bestDate.presentation.base.questionnaire.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bestDate.R
import com.bestDate.presentation.base.BaseFragment
import com.bestDate.presentation.base.questionnaire.QuestionnaireQuestion
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.utils.CityListItem
import com.bestDate.databinding.FragmentSearchQuestionnaireLocationBinding

class SearchQuestionnaireLocationFragment(private val question: QuestionnaireQuestion) :
    BaseFragment<FragmentSearchQuestionnaireLocationBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentSearchQuestionnaireLocationBinding = { inflater, parent, attach ->
        FragmentSearchQuestionnaireLocationBinding.inflate(inflater, parent, attach)
    }

    override val statusBarLight = true
    override val navBarLight = true
    override val navBarColor = R.color.white

    var backClick: (() -> Unit)? = null
    var saveClick: ((String) -> Unit)? = null
    override var customBackNavigation = true
    private var selectedLocation: CityListItem? = null

    override fun onInit() {
        super.onInit()

        with(binding) {
            percentNumber.text = question.questionInfo?.percent.toString()
            binding.searchView.initSearching(
                lifecycleScope,
                viewLifecycleOwner,
                question.answer.orEmpty()
            )
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.onClick = {
                backClick?.invoke()
            }
            saveButton.setOnSaveClickListener {
                saveClick?.invoke(selectedLocation?.getLocation().orEmpty())
            }
            searchView.selectAction = {
                selectedLocation = it
            }
        }
    }

    override fun onCustomBackNavigation() {
        backClick?.invoke()
    }
}