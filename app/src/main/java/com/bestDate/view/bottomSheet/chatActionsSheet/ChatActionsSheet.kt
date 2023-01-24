package com.bestDate.view.bottomSheet.chatActionsSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.databinding.SheetItemListBinding
import com.bestDate.view.base.BaseBottomSheet

class ChatActionsSheet(private val actions: MutableList<ChatActions>) :
    BaseBottomSheet<SheetItemListBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> SheetItemListBinding =
        { inflater, parent, attach -> SheetItemListBinding.inflate(inflater, parent, attach) }

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