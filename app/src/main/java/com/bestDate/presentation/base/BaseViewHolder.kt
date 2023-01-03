package com.bestDate.presentation.base

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bestDate.R
import com.bestDate.data.model.Chat
import com.bestDate.data.model.ChatItemType
import com.bestDate.presentation.base.questionnaire.QuestionnaireQuestion

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

abstract class ChatListBaseViewHolder<VB : ViewBinding>(
    override val binding: VB,
    val itemClick: ((Chat) -> Unit)?
) : BaseViewHolder<Chat, VB>(binding) {

    protected fun getTextColor(itemType: ChatItemType?): Int {
        return ContextCompat.getColor(
            itemView.context,
            if (itemType == ChatItemType.NEW_ITEM) R.color.white_90 else R.color.white_30
        )
    }

    protected fun getTitleColor(itemType: ChatItemType?): Int {
        return ContextCompat.getColor(
            itemView.context,
            if (itemType == ChatItemType.NEW_ITEM) R.color.white_90 else R.color.white_60
        )
    }

    protected fun getMessageIcon(item: Chat): Int {
        return when {
            item.type == ChatItemType.NEW_ITEM -> R.drawable.ic_new_message
            item.last_message?.read_at == null -> return R.drawable.ic_message_not_checked
            else -> R.drawable.ic_message_checked
        }
    }
}

abstract class QuestionnaireBaseViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseClickViewHolder<QuestionnaireQuestion, ((QuestionnaireQuestion) -> Unit)?, VB>(binding)