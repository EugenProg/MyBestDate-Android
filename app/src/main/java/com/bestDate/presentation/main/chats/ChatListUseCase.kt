package com.bestDate.presentation.main.chats

import androidx.lifecycle.MutableLiveData
import com.bestDate.R
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.Chat
import com.bestDate.data.model.ChatItemType
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.Message
import com.bestDate.db.dao.UserDao
import com.bestDate.network.remote.ChatsRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatListUseCase @Inject constructor(
    private val userDao: UserDao,
    private val remoteData: ChatsRemoteData
) {
    val chatList: MutableLiveData<MutableList<Chat>> = MutableLiveData()
    val hasNewChats: MutableLiveData<Boolean> = MutableLiveData(false)
    private var currentList: MutableList<Chat>? = mutableListOf()

    suspend fun refreshChatList() {
        val response = remoteData.getChatList()
        if (response.isSuccessful) {
            response.body()?.let {
                currentList = it.data
            }
            transformChatList(currentList)
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun deleteChat(chatId: Int?) {
        val response = remoteData.deleteChat(chatId.orZero)
        if (response.isSuccessful) {
            currentList?.removeAll { it.user?.id == chatId }
            transformChatList(currentList)
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    private fun transformChatList(list: MutableList<Chat>?) {
        val userId = userDao.getUser()?.id
        val newList: MutableList<Chat> = mutableListOf(createNewHeader())
        val oldList: MutableList<Chat> = mutableListOf(createOldHeader())
        list?.forEach {
            if (it.last_message?.read_at == null && it.last_message?.sender_id != userId) {
                newList.add(it.transform(ChatItemType.NEW_ITEM))
            } else {
                oldList.add(it.transform(ChatItemType.OLD_ITEM))
            }
        }
        hasNewChats.postValue(newList.size > 1)

        when {
            newList.size > 1 && oldList.size > 1 -> {
                newList.addAll(oldList)
                chatList.postValue(newList)
            }
            newList.size == 1 && oldList.size > 1 -> {
                chatList.postValue(oldList)
            }
            newList.size > 1 && oldList.size == 1 -> {
                chatList.postValue(newList)
            }
        }
    }

    private fun createNewHeader(): Chat =
        Chat(id = -2, last_message = Message(id = R.string.new_message), type = ChatItemType.HEADER)

    private fun createOldHeader(): Chat =
        Chat(id = -1, last_message = Message(id = R.string.all_message), type = ChatItemType.HEADER)

    fun clearData() {
        chatList.value = mutableListOf()
        hasNewChats.value = false
    }
}