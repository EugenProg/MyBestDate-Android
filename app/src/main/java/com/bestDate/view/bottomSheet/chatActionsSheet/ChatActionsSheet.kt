package com.bestDate.view.bottomSheet.chatActionsSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.databinding.ItemBottomSheetListBinding
import com.bestDate.databinding.SheetImageListBinding
import com.bestDate.presentation.base.BaseClickViewHolder
import com.bestDate.view.base.BaseBottomSheet

class ChatActionsSheet(private val actions: MutableList<ChatActions>) :
    BaseBottomSheet<SheetImageListBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetImageListBinding =
        { inflater, parent, attach -> SheetImageListBinding.inflate(inflater, parent, attach) }

    var itemClick: ((ChatActions) -> Unit)? = null

    override fun onInit() {
        super.onInit()
        with(binding) {
            title.text = getString(R.string.chat_actions)

            val adapter = ChatActionsAdapter(actions)
            itemList.layoutManager = LinearLayoutManager(requireContext())
            itemList.adapter = adapter

            adapter.onClick = {
                itemClick?.invoke(it)
                dismiss()
            }
        }
    }
}

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