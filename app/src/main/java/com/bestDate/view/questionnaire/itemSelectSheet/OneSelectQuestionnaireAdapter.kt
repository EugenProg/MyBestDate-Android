package com.bestDate.view.questionnaire.itemSelectSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.R
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ItemQuestionnareListBinding

class OneSelectQuestionnaireAdapter(private val selectedItem: String?,
                                    private val itemClick: ((String) -> Unit)):
    ListAdapter<Int, OneSelectQuestionnaireAdapter.OneSelectViewHolder>(AnswerDiffCallback()) {

    private class AnswerDiffCallback: DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    class OneSelectViewHolder(override val binding: ItemQuestionnareListBinding):
        BaseClickViewHolder<Int, ((String) -> Unit), ItemQuestionnareListBinding>(binding) {
        private var answer: String = ""

        override fun bindView(item: Int, itemClick: (String) -> Unit) {
            answer = itemView.context.getString(item)
            binding.name.text = answer

            itemView.setOnSaveClickListener { itemClick.invoke(answer) }
        }

        fun setSelectedItem(item: String?) {
            val color = ContextCompat.getColor(itemView.context,
                if (answer == item) R.color.bg_main else R.color.main_20_without_opacity
            )
            binding.checked.setColorFilter(color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OneSelectViewHolder {
        return OneSelectViewHolder(ItemQuestionnareListBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OneSelectViewHolder, position: Int) {
        holder.bind(getItem(position), itemClick)
        holder.setSelectedItem(selectedItem)
    }

}