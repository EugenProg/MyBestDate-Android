package com.bestDate.presentation.main.chats

import androidx.lifecycle.MutableLiveData
import com.bestDate.R
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.*
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
    var meta: Meta = Meta()
    private var currentList: MutableList<Chat>? = mutableListOf()

    suspend fun refreshChatList() {
        val response = remoteData.getChatList(0)
        if (response.isSuccessful) {
            response.body()?.let {
                currentList = it.data
                meta = it.meta ?: Meta()
            }
            transformChatList(currentList)
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun getNextPage() {
        val page = meta.current_page.orZero + 1

        val response = remoteData.getChatList(page)
        if (response.isSuccessful) {
            response.body()?.let {
                currentList?.addAll(it.data.orEmpty())
                meta = it.meta ?: Meta()
            }
            transformChatList(currentList)
        }
    }

    suspend fun deleteChat(chatId: Int?) {
        val response = remoteData.deleteChat(chatId.orZero)
        if (response.isSuccessful) {
            currentList?.removeAll { it.user?.id == chatId }
            transformChatList(currentList)
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun setMessage(message: Message?) {
        val chatId = currentList?.indexOfFirst {
            it.user?.id == message?.sender_id || it.user?.id == message?.recipient_id
        }
        if (chatId != null && chatId >= 0) {
            currentList?.get(chatId)?.last_message = message
            currentList?.sortByDescending { it.last_message?.created_at }
            transformChatList(currentList)
        } else {
            refreshChatList()
        }
    }

    private fun transformChatList(list: MutableList<Chat>?) {
        val userId = userDao.getUser()?.id
        var newChats = 0
        val chatsList: MutableList<Chat> = mutableListOf()
        //val newList: MutableList<Chat> = mutableListOf(createNewHeader())
       // val oldList: MutableList<Chat> = mutableListOf(createOldHeader())
        list?.forEach {
            if (it.last_message?.read_at == null && it.last_message?.sender_id != userId) {
                newChats++
                chatsList.add(it.transform(ListItemType.NEW_ITEM))
            } else {
                chatsList.add(it.transform(ListItemType.OLD_ITEM))
            }
        }
        hasNewChats.postValue(newChats > 0)
        chatList.postValue(chatsList)

      /*  when {
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
        }*/
    }

    private fun createNewHeader(): Chat =
        Chat(id = -2, last_message = Message(id = R.string.new_message), type = ListItemType.HEADER)

    private fun createOldHeader(): Chat =
        Chat(id = -1, last_message = Message(id = R.string.all_message), type = ListItemType.HEADER)

    fun clearData() {
        chatList.postValue(mutableListOf())
        hasNewChats.postValue(false)
        currentList = null
    }

    fun setTypingEvent(senderId: Int?, isOn: Boolean) {
        synchronized(this) {
            val list: MutableList<Chat> = mutableListOf()
            chatList.value?.forEach {
                if (it.user?.id == senderId) {
                    list.add(it.copy(typingMode = isOn))
                } else list.add(it)
            }

            chatList.postValue(list)
        }
    }
}