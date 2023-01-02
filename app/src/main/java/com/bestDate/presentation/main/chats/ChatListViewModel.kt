package com.bestDate.presentation.main.chats

import androidx.lifecycle.asLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val chatsListUseCase: ChatListUseCase
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()
    val chatList = chatsListUseCase.chatList

    fun refreshChatList() {
        doAsync {
            chatsListUseCase.refreshChatList()
        }
    }

    fun deleteChat(chatId: Int) {
        doAsync {
            chatsListUseCase.deleteChat(chatId)
        }
    }
}