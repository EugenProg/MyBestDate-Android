package com.bestDate.presentation.main.chats

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.Chat
import com.bestDate.data.model.InternalException
import com.bestDate.network.remote.ChatsRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatListUseCase @Inject constructor(
    private val remoteData: ChatsRemoteData
) {
    val chatList: MutableLiveData<MutableList<Chat>> = MutableLiveData()

    suspend fun refreshChatList() {
        val response = remoteData.getChatList()
        if (response.isSuccessful) {
            response.body()?.let {
                chatList.postValue(transformChatList(it.data))
            }
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun deleteChat(chatId: Int) {
        val response = remoteData.deleteChat(chatId)
        if (response.isSuccessful) {
            val list: MutableList<Chat> = mutableListOf()
            chatList.value?.forEach {
                if (it.id != chatId) list.add(it)
            }
            chatList.postValue(list)
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    private fun transformChatList(list: MutableList<Chat>?): MutableList<Chat> {
        val newList: MutableList<Chat> = mutableListOf()
        list?.forEach {
            newList.add(it.transform())
        }
        return newList
    }
}