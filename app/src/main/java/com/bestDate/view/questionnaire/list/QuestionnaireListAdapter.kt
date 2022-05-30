package com.bestDate.view.questionnaire.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.base.QuestionnaireBaseViewHolder
import com.bestDate.data.extension.orZero
import com.bestDate.fragment.questionnarie.QuestionnaireViewHolderFactory

class QuestionnaireListAdapter(private val itemClick: ((QuestionnaireQuestion) -> Unit)):
    ListAdapter<QuestionnaireQuestion, QuestionnaireBaseViewHolder<*>>(QuestionnaireDiffCallback()) {

        private class QuestionnaireDiffCallback: DiffUtil.ItemCallback<QuestionnaireQuestion>() {
            override fun areItemsTheSame(
                oldItem: QuestionnaireQuestion,
                newItem: QuestionnaireQuestion
            ): Boolean {
                return oldItem.questionInfo == newItem.questionInfo
            }

            override fun areContentsTheSame(
                oldItem: QuestionnaireQuestion,
                newItem: QuestionnaireQuestion
            ): Boolean {
                return oldItem == newItem
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionnaireBaseViewHolder<*> {
        return QuestionnaireViewHolderFactory().getViewHolder(viewType, parent)
    }

    override fun onBindViewHolder(holder: QuestionnaireBaseViewHolder<*>, position: Int) {
        holder.bind(getItem(position), itemClick)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).questionInfo?.viewType?.id.orZero
    }
}