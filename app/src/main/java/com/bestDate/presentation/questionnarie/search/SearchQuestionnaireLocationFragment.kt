package com.bestDate.presentation.questionnarie.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.base.BaseFragment
import com.bestDate.databinding.FragmentSearchQuestionnaireLocationBinding
import com.bestDate.view.questionnaire.list.QuestionnaireQuestion
import com.bestDate.view.searchLocation.SearchLocationView
import com.bestDate.view.searchLocation.SearchResultsAdapter

class SearchQuestionnaireLocationFragment(private val question: QuestionnaireQuestion) :
    BaseFragment<FragmentSearchQuestionnaireLocationBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) ->
    FragmentSearchQuestionnaireLocationBinding = { inflater, parent, attach ->
        FragmentSearchQuestionnaireLocationBinding.inflate(inflater, parent, attach)
    }

    override val statusBarLight = true
    override val navBarLight = true
    override val navBarColor = R.color.white

    private lateinit var adapter: SearchResultsAdapter
    private var addressesList: MutableLiveData<MutableList<String>> = MutableLiveData()

    var backClick: (() -> Unit)? = null
    var saveClick: ((String) -> Unit)? = null
    override var customBackNavigation = true

    override fun onInit() {
        super.onInit()

        with(binding) {
            searchInput.hint = getString(R.string.specify_the_search_location)
            searchInput.flowTextChangeInvoker(lifecycleScope)

            percentNumber.text = question.questionInfo?.percent.toString()
            searchInput.text = question.answer.orEmpty()

            searchInput.textIsChangedFlow = {
                search(it)
            }

            adapter = SearchResultsAdapter(SearchLocationView.SearchStyle.LIGHT)
            addressList.layoutManager = LinearLayoutManager(context)
            addressList.adapter = adapter

            searchInput.setFocus()
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            backButton.setOnClickListener { backClick?.invoke() }
            saveButton.setOnClickListener { saveClick?.invoke(searchInput.text) }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
       /* addressesList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }*/
    }

    private fun search(text: String) {

    }

    override fun onCustomBackNavigation() {
        super.onCustomBackNavigation()
        backClick?.invoke()
    }

    private fun itemClick(): (String) -> Unit {
        return {
            binding.searchInput.text = it
        }
    }

    override fun scrollAction() {
        super.scrollAction()
        binding.searchInput.setFocus()
    }
}