package com.bestDate.presentation.main.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private var _deleteLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var deleteLiveData: LiveData<Boolean> = _deleteLiveData

    fun refreshChatList() {
        doAsync {
            chatsListUseCase.refreshChatList()
        }
    }

    fun deleteChat(chatId: Int?) {
        _deleteLiveData.value = true
        doAsync {
            chatsListUseCase.deleteChat(chatId)
            _deleteLiveData.postValue(false)
        }
    }
}