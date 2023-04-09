package com.bestDate.view.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bestDate.R
import com.bestDate.data.extension.getTime
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.model.ChatImage
import com.bestDate.data.model.ChatItemType
import com.bestDate.data.model.Message
import com.bestDate.data.model.Meta
import com.bestDate.databinding.*
import com.bestDate.presentation.base.ChatBaseViewHolder
import com.bumptech.glide.Glide

class ChatAdapter : ListAdapter<Message, ChatBaseViewHolder<*>>(ChatDiffUtil()) {

    var messageClick: ((Message) -> Unit)? = null
    var imageOpenClick: ((ChatImage?) -> Unit)? = null
    var imageLongClick: ((Message) -> Unit)? = null
    var loadMoreItems: (() -> Unit)? = null
    var meta: Meta? = Meta()
    var loadingMode: Boolean = false

    class ChatDiffUtil : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatBaseViewHolder<*> {
        return when (viewType) {
            ChatItemType.DATE.ordinal ->
                DateViewHolder(
                    ItemChatDateViewBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            ChatItemType.MY_TEXT_MESSAGE.ordinal -> {
                MyTextMessageViewHolder(
                    ItemMyTextMessageBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ), messageClick
                )
            }
            ChatItemType.MY_IMAGE_MESSAGE.ordinal -> {
                MyImageMessageViewHolder(
                    ItemMyImageMessageBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ), messageClick, imageOpenClick, imageLongClick
                )
            }
            ChatItemType.USER_TEXT_MESSAGE.ordinal -> {
                UserTextMessageViewHolder(
                    ItemUserTextMessageBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ), messageClick
                )
            }
            ChatItemType.USER_IMAGE_MESSAGE.ordinal -> {
                UserImageMessageViewHolder(
                    ItemUserImageMessageBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ), messageClick, imageOpenClick, imageLongClick
                )
            }
            ChatItemType.LOADER.ordinal -> {
                LoaderViewHolder(
                    ItemChatLoaderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            else -> UserTextMessageViewHolder(
                ItemUserTextMessageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), messageClick
            )
        }
    }

    override fun onBindViewHolder(holder: ChatBaseViewHolder<*>, position: Int) {
        holder.bind(getItem(position))

        if (position >= itemCount - 1 && meta?.current_page.orZero < meta?.last_page.orZero && !loadingMode) {
            setLoadingMode()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType?.ordinal.orZero
    }

    class MyTextMessageViewHolder(
        override val binding: ItemMyTextMessageBinding,
        itemClick: ((Message) -> Unit)?
    ) :
        ChatBaseViewHolder<ItemMyTextMessageBinding>(binding, itemClick) {
        override fun bindView(item: Message) {
            super.bindView(item)
            with(binding) {
                parentMessageBox.isVisible = item.parentMessage != null
                parentLine.isVisible = item.parentMessage != null
                parentMessage.text = item.parentMessage?.text
                parentImageBox.isVisible = item.parentMessage?.image != null
                item.parentMessage?.image?.let {
                    Glide.with(itemView.context)
                        .load(it.thumb_url)
                        .into(parentImage)
                }

                message.text = item.text
                timeBox.isVisible = item.isLastMessage == true
                time.text = item.created_at.getTime()
                status.setImageResource(
                    if (item.read_at == null) R.drawable.ic_message_not_checked
                    else R.drawable.ic_message_checked
                )

                messageBox.setOnSaveClickListener {
                    itemClick?.invoke(item)
                }
            }
        }
    }

    class MyImageMessageViewHolder(
        override val binding: ItemMyImageMessageBinding,
        itemClick: ((Message) -> Unit)?,
        private val imageOpen: ((ChatImage?) -> Unit)?,
        private val itemLongClick: ((Message) -> Unit)?
    ) :
        ChatBaseViewHolder<ItemMyImageMessageBinding>(binding, itemClick) {
        override fun bindView(item: Message) {
            super.bindView(item)
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.image?.thumb_url)
                    .into(imageView)

                message.isVisible = item.text != null
                message.text = item.text
                timeBox.isVisible = item.isLastMessage == true
                time.text = item.created_at.getTime()
                status.setImageResource(
                    if (item.read_at == null) R.drawable.ic_message_not_checked
                    else R.drawable.ic_message_checked
                )

                messageBox.setOnSaveClickListener {
                    itemClick?.invoke(item)
                }
                imageView.setOnSaveClickListener {
                    imageOpen?.invoke(item.image)
                }
                imageView.setOnLongClickListener {
                    itemLongClick?.invoke(item)
                    true
                }
            }
        }
    }

    class UserTextMessageViewHolder(
        override val binding: ItemUserTextMessageBinding,
        itemClick: ((Message) -> Unit)?
    ) :
        ChatBaseViewHolder<ItemUserTextMessageBinding>(binding, itemClick) {
        override fun bindView(item: Message) {
            super.bindView(item)
            with(binding) {
                parentMessageBox.isVisible = item.parentMessage != null
                parentLine.isVisible = item.parentMessage != null
                parentMessage.text = item.parentMessage?.text
                parentImageBox.isVisible = item.parentMessage?.image != null
                item.parentMessage?.image?.let {
                    Glide.with(itemView.context)
                        .load(it.thumb_url)
                        .into(parentImage)
                }

                message.text = item.text
                time.isVisible = item.isLastMessage == true
                time.text = item.created_at.getTime()

                messageBox.setOnSaveClickListener {
                    itemClick?.invoke(item)
                }
            }
        }
    }

    class UserImageMessageViewHolder(
        override val binding: ItemUserImageMessageBinding,
        itemClick: ((Message) -> Unit)?,
        private val imageOpen: ((ChatImage?) -> Unit)?,
        private val itemLongClick: ((Message) -> Unit)?
    ) :
        ChatBaseViewHolder<ItemUserImageMessageBinding>(binding, itemClick) {
        override fun bindView(item: Message) {
            super.bindView(item)
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.image?.thumb_url)
                    .into(imageView)

                message.isVisible = item.text != null
                message.text = item.text
                time.isVisible = item.isLastMessage == true
                time.text = item.created_at.getTime()

                messageBox.setOnSaveClickListener {
                    itemClick?.invoke(item)
                }
                imageView.setOnSaveClickListener {
                    imageOpen?.invoke(item.image)
                }
                imageView.setOnLongClickListener {
                    itemLongClick?.invoke(item)
                    true
                }
            }
        }
    }

    class DateViewHolder(override val binding: ItemChatDateViewBinding) :
        ChatBaseViewHolder<ItemChatDateViewBinding>(binding, null) {
        override fun bindView(item: Message) {
            super.bindView(item)
            binding.dateView.text = item.getDate(itemView.context)
        }
    }

    class LoaderViewHolder(override val binding: ItemChatLoaderBinding) :
        ChatBaseViewHolder<ItemChatLoaderBinding>(binding, null)

    private fun setLoadingMode() {
        val newList: MutableList<Message> = mutableListOf()
        newList.addAll(currentList)
        newList.add(loadingItem)
        submitList(newList)
        loadingMode = true
        loadMoreItems?.invoke()
    }

    override fun submitList(list: MutableList<Message>?) {
        loadingMode = false
        super.submitList(list)
    }

    private var loadingItem: Message =
        Message(id = 0, viewType = ChatItemType.LOADER)
}