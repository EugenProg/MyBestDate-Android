package com.bestDate.view.questionnaire.itemSelectSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.R
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bestDate.databinding.ItemQuestionnareListBinding

class MultiSelectQuestionnaireAdapter(private val itemClick: ((String) -> Unit)):
    ListAdapter<QuestionnaireAnswer, MultiSelectQuestionnaireAdapter.MultiSelectViewHolder>(AnswerDiffCallback()) {

    private class AnswerDiffCallback: DiffUtil.ItemCallback<QuestionnaireAnswer>() {
        override fun areItemsTheSame(oldItem: QuestionnaireAnswer, newItem: QuestionnaireAnswer): Boolean {
            return oldItem.answer == newItem.answer
        }

        override fun areContentsTheSame(oldItem: QuestionnaireAnswer, newItem: QuestionnaireAnswer): Boolean {
            return oldItem == newItem
        }
    }

    class MultiSelectViewHolder(override val binding: ItemQuestionnareListBinding):
        BaseClickViewHolder<QuestionnaireAnswer, ((String) -> Unit), ItemQuestionnareListBinding>(binding) {
        private var answer: String = ""

        override fun bindView(item: QuestionnaireAnswer, itemClick: (String) -> Unit) {
            answer = itemView.context.getString(item.answer)
            binding.name.text = answer

            val checkColor = getActiveColor(item.isSelected)
            binding.checked.setColorFilter(checkColor)

            itemView.setOnClickListener {
                item.isSelected = !item.isSelected
                val color = getActiveColor(item.isSelected)
                binding.checked.setColorFilter(color)
                itemClick.invoke(answer)
            }
        }

        private fun getActiveColor(active: Boolean): Int {
            return ContextCompat.getColor(itemView.context,
                if (active) R.color.bg_main else R.color.main_20_without_opacity
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiSelectViewHolder {
        return MultiSelectViewHolder(
            ItemQuestionnareListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MultiSelectViewHolder, position: Int) {
        holder.bind(getItem(position), itemClick)
    }
}