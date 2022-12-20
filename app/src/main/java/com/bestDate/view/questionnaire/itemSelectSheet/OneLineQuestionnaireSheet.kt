package com.bestDate.view.questionnaire.itemSelectSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.base.questionnaire.QuestionnaireQuestion
import com.bestDate.data.extension.orZero
import com.bestDate.databinding.SheetOneLineQuestionnaireBinding
import com.bestDate.view.base.BaseBottomSheet

class OneLineQuestionnaireSheet : BaseBottomSheet<SheetOneLineQuestionnaireBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetOneLineQuestionnaireBinding =
        { inflater, parent, attach ->
            SheetOneLineQuestionnaireBinding.inflate(
                inflater,
                parent,
                attach
            )
        }

    private lateinit var question: QuestionnaireQuestion

    override fun getTheme() = R.style.WhiteBottomSheetDialogTheme

    private var itemList: MutableLiveData<IntArray> = MutableLiveData()
    private lateinit var adapter: OneSelectQuestionnaireAdapter
    var itemClick: ((String) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        adapter = OneSelectQuestionnaireAdapter(question.answer) { itemClick?.invoke(it) }
        with(binding) {
            title.text = getString(question.questionInfo?.question.orZero)
            percentNumber.text = question.questionInfo?.percent.orZero.toString()

            itemList.layoutManager = LinearLayoutManager(context)
            itemList.adapter = adapter

            setItems(question.questionInfo?.answers)
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        itemList.observe(viewLifecycleOwner) {
            adapter.submitList(it.toMutableList())
        }
    }

    fun setInfo(question: QuestionnaireQuestion) {
        this.question = question
    }

    private fun setItems(answers: IntArray?) {
        answers?.let { itemList.value = it }
    }
}