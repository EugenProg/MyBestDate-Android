package com.bestDate.presentation.main.chats.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.databinding.FragmentChatBinding
import com.bestDate.presentation.base.BaseVMFragment

class ChatFragment: BaseVMFragment<FragmentChatBinding, ChatViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChatBinding =
        { inflater, parent, attach -> FragmentChatBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<ChatViewModel> = ChatViewModel::class.java

}