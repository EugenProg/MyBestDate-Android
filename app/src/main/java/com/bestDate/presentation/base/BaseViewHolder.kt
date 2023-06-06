package com.bestDate.presentation.base

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bestDate.R
import com.bestDate.data.model.*
import com.bestDate.presentation.base.questionnaire.QuestionnaireQuestion

abstract class BaseViewHolder<T, VB : ViewBinding>(open val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {
    open fun bind(item: T) {
        bindView(item)
    }

    open fun bindView(item: T) {}
}

abstract class BaseClickViewHolder<T, IC, VB : ViewBinding>(override val binding: VB) :
    BaseViewHolder<T, VB>(binding) {
    fun bind(item: T, itemClick: IC) {
        bindView(item, itemClick)
    }

    abstract fun bindView(item: T, itemClick: IC)
}

abstract class ChatListBaseViewHolder<VB : ViewBinding>(
    override val binding: VB,
    val itemClick: ((Chat) -> Unit)?
) : BaseViewHolder<Chat, VB>(binding) {

    protected fun getTextColor(itemType: ListItemType?, typingMode: Boolean?): Int {
        return ContextCompat.getColor(
            itemView.context,
            when {
                typingMode == true -> R.color.green
                itemType == ListItemType.NEW_ITEM -> R.color.white_90
                else -> R.color.white_30
            }
        )
    }

    protected fun getTitleColor(itemType: ListItemType?): Int {
        return ContextCompat.getColor(
            itemView.context,
            if (itemType == ListItemType.NEW_ITEM) R.color.white_90 else R.color.white_60
        )
    }

    protected fun getMessageIcon(item: Chat): Int {
        return when {
            item.type == ListItemType.NEW_ITEM -> R.drawable.ic_new_message
            item.last_message?.read_at == null -> return R.drawable.ic_message_not_checked
            else -> R.drawable.ic_message_checked
        }
    }
}

abstract class GuestBaseViewHolder<VB : ViewBinding>(
    override val binding: VB,
    open val itemClick: ((Guest) -> Unit)?
) : BaseViewHolder<Guest, VB>(binding)

abstract class ChatBaseViewHolder<VB : ViewBinding>(
    override val binding: VB,
    val itemClick: ((Message) -> Unit)?
) : BaseViewHolder<Message, VB>(binding)

abstract class InvitationBaseViewHolder<VB : ViewBinding>(
    override val binding: VB
) : BaseViewHolder<InvitationCard, VB>(binding)

abstract class QuestionnaireBaseViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseClickViewHolder<QuestionnaireQuestion, ((QuestionnaireQuestion) -> Unit)?, VB>(binding)

abstract class ImageLineBaseViewHolder<VB : ViewBinding, IC>(override val binding: VB) :
    BaseClickViewHolder<ProfileImage, IC, VB>(binding)