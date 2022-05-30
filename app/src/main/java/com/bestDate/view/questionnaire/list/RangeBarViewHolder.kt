package com.bestDate.view.questionnaire.list

import com.bestDate.base.QuestionnaireBaseViewHolder
import com.bestDate.data.extension.orZero
import com.bestDate.databinding.ItemRangeBarQuestionnaireBinding
import com.google.gson.Gson
import java.lang.Exception
import kotlin.math.max

class RangeBarViewHolder(override val binding: ItemRangeBarQuestionnaireBinding):
    QuestionnaireBaseViewHolder<ItemRangeBarQuestionnaireBinding>(binding) {
    override fun bindView(
        item: QuestionnaireQuestion,
        itemClick: ((QuestionnaireQuestion) -> Unit)?
    ) {
        with(binding.view) {
            hint = context.getString(item.questionInfo?.question.orZero)

            val answers = item.questionInfo?.answers
            if (answers?.isNotEmpty() == true) minProgress = answers[0]
            if (answers?.size.orZero > 1) maxProgress = answers?.get(1).orZero

            if (item.answer != null) {
                range = item.answer.orEmpty()
            } else {
                startProgress = minProgress + 10
                endProgress = maxProgress - 10
            }

            rangeIsChanged = {
               item.answer = it
               itemClick?.invoke(item)
            }
        }
    }
}