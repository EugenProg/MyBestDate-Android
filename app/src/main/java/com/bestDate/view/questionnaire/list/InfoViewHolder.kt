package com.bestDate.view.questionnaire.list

import com.bestDate.base.QuestionnaireBaseViewHolder
import com.bestDate.base.questionnaire.QuestionnaireQuestion
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ItemMultilineQuestionInfoBinding

class InfoViewHolder(override val binding: ItemMultilineQuestionInfoBinding) :
    QuestionnaireBaseViewHolder<ItemMultilineQuestionInfoBinding>(binding) {

    override fun bindView(
        item: QuestionnaireQuestion,
        itemClick: ((QuestionnaireQuestion) -> Unit)?
    ) {
        with(binding.view) {
            hint = context.getString(item.questionInfo?.question.orZero)
            info = if (item.questionInfo?.unitString != null && item.answer != null) {
                context.getString(item.questionInfo?.unitString.orZero, item.answer)
            } else item.answer

            percent = item.questionInfo?.percent.orZero
            setOnSaveClickListener { itemClick?.invoke(item) }
        }
    }
}