package com.bestDate.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bestDate.base.questionnaire.QuestionnaireQuestion

abstract class BaseViewHolder<T, VB : ViewBinding>(open val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {
    open fun bind(item: T) {
        itemView.apply { bindView(item) }
    }

    open fun bindView(item: T) {}
}

abstract class BaseClickViewHolder<T, IC, VB : ViewBinding>(override val binding: VB) :
    BaseViewHolder<T, VB>(binding) {
    fun bind(item: T, itemClick: IC) {
        itemView.apply { bindView(item, itemClick) }
    }

    abstract fun bindView(item: T, itemClick: IC)
}

abstract class QuestionnaireBaseViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseClickViewHolder<QuestionnaireQuestion, ((QuestionnaireQuestion) -> Unit)?, VB>(binding) {
}