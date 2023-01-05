package com.bestDate.presentation.main.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.Chat
import com.bestDate.data.model.ChatItemType
import com.bestDate.databinding.ItemChatBotBinding
import com.bestDate.databinding.ItemChatListBinding
import com.bestDate.databinding.ItemChatListHeaderBinding
import com.bestDate.presentation.base.ChatListBaseViewHolder
import com.bumptech.glide.Glide

class ChatListAdapter : ListAdapter<Chat, ChatListBaseViewHolder<*>>(ChatListDiffUtil()) {

    var clickAction: ((Chat) -> Unit)? = null

    class ChatListDiffUtil : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }

    class ChatListItemViewHolder(
        override val binding: ItemChatListBinding,
        itemClick: ((Chat) -> Unit)?
    ) : ChatListBaseViewHolder<ItemChatListBinding>(binding, itemClick) {
        override fun bind(item: Chat) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.user?.getMainPhoto()?.thumb_url)
                    .circleCrop()
                    .into(avatar)
                online.isVisible = item.user?.is_online == true
                name.text = item.user?.name
                lastMessage.text = item.last_message?.text
                time.text = item.getLastMessageTime()

                val imageVisibility = item.last_message?.image != null
                lastMessage.isVisible = !imageVisibility
                pictureBox.isVisible = imageVisibility

                read.setImageResource(getMessageIcon(item))

                name.setTextColor(getTitleColor(item.type))
                lastMessage.setTextColor(getTextColor(item.type))

                root.setOnSaveClickListener {
                    itemClick?.invoke(item)
                }
            }
        }
    }

    class HeaderViewHolder(override val binding: ItemChatListHeaderBinding) :
        ChatListBaseViewHolder<ItemChatListHeaderBinding>(binding, null) {
        override fun bind(item: Chat) {
            super.bind(item)
            binding.root.text = item.last_message?.id?.let { itemView.context.getString(it) }
        }
    }

    class BotViewHolder(
        override val binding: ItemChatBotBinding,
        itemClick: ((Chat) -> Unit)?
    ) : ChatListBaseViewHolder<ItemChatBotBinding>(binding, itemClick) {
        override fun bind(item: Chat) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(R.mipmap.ic_launcher)
                    .circleCrop()
                    .into(avatar)

                lastMessage.text = item.last_message?.text
                time.text = item.getLastMessageTime()

                read.setImageResource(getMessageIcon(item))

                name.setTextColor(getTitleColor(item.type))
                lastMessage.setTextColor(getTextColor(item.type))

                root.setOnSaveClickListener {
                    itemClick?.invoke(item)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type?.ordinal ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListBaseViewHolder<*> {
        return when (viewType) {
            ChatItemType.HEADER.ordinal -> {
                HeaderViewHolder(
                    ItemChatListHeaderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            ChatItemType.BOT.ordinal -> {
                BotViewHolder(
                    ItemChatBotBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ), clickAction
                )
            }
            else -> {
                ChatListItemViewHolder(
                    ItemChatListBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ), clickAction
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ChatListBaseViewHolder<*>, position: Int) {
        holder.bind(getItem(position))
    }
}