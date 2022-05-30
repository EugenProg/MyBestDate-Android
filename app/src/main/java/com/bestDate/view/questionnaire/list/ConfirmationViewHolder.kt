package com.bestDate.view.questionnaire.list

import com.bestDate.base.QuestionnaireBaseViewHolder
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ItemConfirmationBinding

class ConfirmationViewHolder(override val binding: ItemConfirmationBinding):
    QuestionnaireBaseViewHolder<ItemConfirmationBinding>(binding) {
    override fun bindView(
        item: QuestionnaireQuestion,
        itemClick: ((QuestionnaireQuestion) -> Unit)?
    ) {
        with(binding.view) {
            title = context.getString(item.questionInfo?.question.orZero)
            description = if (item.answer.isNullOrBlank()) {
                if (item.questionInfo?.answers?.isNotEmpty() == true) {
                    context.getString(item.questionInfo?.answers?.get(0).orZero)
                } else ""
            } else {
                item.answer.orEmpty()
            }

            isConfirm = !item.answer.isNullOrBlank()

            setOnSaveClickListener { itemClick?.invoke(item) }
        }
    }

}