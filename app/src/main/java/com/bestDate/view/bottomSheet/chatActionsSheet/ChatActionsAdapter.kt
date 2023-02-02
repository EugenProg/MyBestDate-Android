package com.bestDate.view.bottomSheet.chatActionsSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ItemBottomSheetListBinding
import com.bestDate.presentation.base.BaseClickViewHolder

class ChatActionsAdapter(private val actions: MutableList<ChatActions>) :
    RecyclerView.Adapter<ChatActionsAdapter.ChatActionsViewHolder>() {

    var onClick: ((ChatActions) -> Unit)? = null

    class ChatActionsViewHolder(override var binding: ItemBottomSheetListBinding) :
        BaseClickViewHolder<ChatActions, ((ChatActions) -> Unit)?, ItemBottomSheetListBinding>(
            binding
        ) {
        override fun bindView(item: ChatActions, itemClick: ((ChatActions) -> Unit)?) {
            binding.name.text = itemView.context.getString(item.actionName)

            itemView.setOnSaveClickListener {
                itemClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatActionsViewHolder {
        return ChatActionsViewHolder(
            ItemBottomSheetListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatActionsViewHolder, position: Int) {
        holder.bind(actions[position], onClick)
    }

    override fun getItemCount(): Int = actions.size
}

enum class ChatActions(@StringRes val actionName: Int) {
    REPLY(R.string.reply),
    EDIT(R.string.edit),
    DELETE(R.string.delete)
}