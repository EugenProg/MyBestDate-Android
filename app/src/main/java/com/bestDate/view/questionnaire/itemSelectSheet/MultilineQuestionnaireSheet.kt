package com.bestDate.view.questionnaire.itemSelectSheet

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.presentation.base.questionnaire.QuestionnaireQuestion
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.SheetMultilineQuestionnaireBinding
import com.bestDate.view.base.BaseBottomSheet

class MultilineQuestionnaireSheet : BaseBottomSheet<SheetMultilineQuestionnaireBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetMultilineQuestionnaireBinding =
        { inflater, parent, attach ->
            SheetMultilineQuestionnaireBinding.inflate(
                inflater,
                parent,
                attach
            )
        }

    private lateinit var question: QuestionnaireQuestion
    private var answersList: MutableList<String> = ArrayList()

    override fun getTheme() = R.style.WhiteBottomSheetDialogTheme

    private var itemList: MutableLiveData<MutableList<QuestionnaireAnswer>> = MutableLiveData()
    private lateinit var adapter: MultiSelectQuestionnaireAdapter
    var onSave: ((items: String) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        adapter = MultiSelectQuestionnaireAdapter(itemClick())
        with(binding) {
            title.text = getString(question.questionInfo?.question.orZero)

            itemList.layoutManager = LinearLayoutManager(context)
            itemList.adapter = adapter

            setItems(question.questionInfo?.answers)
        }
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.saveButton.setOnSaveClickListener { save() }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(itemList) {
            adapter.submitList(it)
        }
    }

    private fun itemClick(): ((String) -> Unit) {
        return {
            val index = answersList.indexOfFirst { a -> a == it }
            if (index >= 0) answersList.removeAt(index)
            else answersList.add(it)
        }
    }

    fun setInfo(question: QuestionnaireQuestion) {
        this.question = question
    }

    private fun setItems(answers: IntArray?) {
        val list: MutableList<QuestionnaireAnswer> = ArrayList()
        answersList.clear()
        for (answer in answers ?: IntArray(0)) {
            val line = getString(answer)
            val isSelect = question.answer?.contains(line) == true
            if (isSelect) answersList.add(line)
            list.add(QuestionnaireAnswer(answer, isSelect))
        }
        itemList.value = list
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        save()
    }

    private fun save() {
        val answer = answersList.joinToString()
        onSave?.invoke(answer)
    }
}