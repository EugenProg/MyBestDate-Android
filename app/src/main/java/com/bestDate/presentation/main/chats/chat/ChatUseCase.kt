package com.bestDate.presentation.main.chats.chat

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getDateString
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.*
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.db.dao.UserDao
import com.bestDate.network.remote.ChatsRemoteData
import com.bestDate.network.remote.TranslationRemoteData
import com.bestDate.presentation.main.chats.ChatListUseCase
import com.bestDate.view.chat.ChatStatusType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatUseCase @Inject constructor(
    private val userDao: UserDao,
    private val chatsRemoteData: ChatsRemoteData,
    private val chatListUseCase: ChatListUseCase,
    private val translateRemoteData: TranslationRemoteData,
    private val preferencesUtils: PreferencesUtils
) {
    var messages: MutableLiveData<MutableList<Message>?> = MutableLiveData()
    var typingMode: MutableLiveData<ChatStatusType> = MutableLiveData()
    var translatedText: String? = ""
    var meta: Meta = Meta()
    private var originalList: MutableList<Message>? = null
    private var myId: Int? = null
    private var currentUserId: Int? = null

    suspend fun sendTextMessage(recipientId: Int?, parentId: Int?, text: String) {
        val response = chatsRemoteData.sendTextMessage(recipientId.orZero, parentId, text)
        if (response.isSuccessful) {
            response.body()?.data?.let {
                preferencesUtils.saveInt(
                    Preferences.SENT_MESSAGES_TODAY,
                    it.sent_messages_today.orZero
                )
                messages.postValue(addNewMessage(it))
                chatListUseCase.setMessage(it)
            }
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun sendImageMessage(
        recipientId: Int?,
        text: String?,
        image: ByteArray
    ) {
        val requestFile =
            image.toRequestBody("multipart/form-data".toMediaTypeOrNull(), 0, image.size)
        val body = MultipartBody.Part.createFormData("image", "name", requestFile)
        val response = chatsRemoteData.sendImageMessage(recipientId.orZero, body, text)
        if (response.isSuccessful) {
            response.body()?.data?.let {
                preferencesUtils.saveInt(
                    Preferences.SENT_MESSAGES_TODAY,
                    it.sent_messages_today.orZero
                )
                messages.postValue(addNewMessage(it))
                chatListUseCase.setMessage(it)
            }
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun editMessage(messageId: Int?, text: String) {
        val response = chatsRemoteData.updateMessage(messageId.orZero, text)
        if (response.isSuccessful) {
            response.body()?.data?.let {
                messages.postValue(editMessageList(it))
                chatListUseCase.setMessage(it)
            }
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun deleteMessage(messageId: Int?) {
        val response = chatsRemoteData.deleteMessage(messageId.orZero)
        if (response.isSuccessful) {
            messages.postValue(deleteMessageFromList(messageId))
            chatListUseCase.refreshChatList()
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun sendTypingEvent() {
        val response = chatsRemoteData.sendTypingEvent(currentUserId.orZero)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody().getErrorMessage()
        )
    }

    suspend fun sendReadingEvent(recipientId: Int?) {
        val response = chatsRemoteData.sendReadingEvent(recipientId.orZero)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody().getErrorMessage()
        )
    }

    suspend fun getChatMessages(userId: Int?) {
        currentUserId = userId
        val response = chatsRemoteData.getChatMessages(userId.orZero, 0)
        if (response.isSuccessful) {
            response.body()?.let {
                originalList = it.data
                meta = it.meta ?: Meta()
                messages.postValue(transformMessageList(it.data))
            }
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun getNextPage() {
        val page = meta.current_page.orZero + 1

        val response = chatsRemoteData.getChatMessages(currentUserId.orZero, page)
        if (response.isSuccessful) {
            response.body()?.let {
                originalList?.addAll(it.data.orEmpty())
                meta = it.meta ?: Meta()
                messages.postValue(transformMessageList(originalList))
            }
        }
    }

    suspend fun translate(text: String?, language: String?) {
        val response = translateRemoteData.translate(text.orEmpty(), (language ?: "EN").uppercase())
        if (response.isSuccessful) {
            translatedText = response.body()?.translations?.firstOrNull()?.text ?: text
        } else throw InternalException.OperationException(response.message())
    }

    suspend fun translateMessage(message: Message) {
        val myLanguage = userDao.getUser()?.language
        val response =
            translateRemoteData.translate(message.text.orEmpty(), (myLanguage ?: "EN").uppercase())
        if (response.isSuccessful) {
            val newMessage = message.copy(
                translatedText = response.body()?.translations?.firstOrNull()?.text,
                translateStatus = Message.TranslateStatus.TRANSLATED
            )
            messages.postValue(editMessageList(newMessage))
        } else throw InternalException.OperationException(response.message())
    }

    fun returnMessage(message: Message) {
        val newMessage = message.copy(
            translatedText = null,
            translateStatus = Message.TranslateStatus.UN_ACTIVE
        )
        messages.postValue(editMessageList(newMessage))
    }

    fun addPusherMessage(message: Message?) {
        message?.let {
            messages.postValue(addNewMessage(it))
        }
    }

    fun editPusherMessage(message: Message?) {
        message?.let {
            messages.postValue(editMessageList(it))
        }
    }

    fun deletePusherMessage(message: Message?) {
        message?.let {
            messages.postValue(deleteMessageFromList(it.id))
        }
    }

    fun clearChatData() {
        messages.value = null
        currentUserId = null
        myId = null
    }

    private fun addNewMessage(message: Message): MutableList<Message> {
        if (originalList?.firstOrNull()?.id != message.id) originalList?.add(0, message)
        return transformMessageList(originalList)
    }

    private fun editMessageList(message: Message): MutableList<Message> {
        originalList?.indexOfFirst { it.id == message.id }?.let {
            originalList!![it] = message
        }
        return transformMessageList(originalList)
    }

    private fun deleteMessageFromList(messageId: Int?): MutableList<Message> {
        originalList?.removeAll { it.id == messageId }
        return transformMessageList(originalList)
    }

    fun isCurrentUserChat(userId: Int?) = userId != null && userId == currentUserId

    private fun transformMessageList(messages: MutableList<Message>?): MutableList<Message> {
        val list: MutableList<Message> = mutableListOf()
        getUser()
        var dateId = -1

        messages?.forEachIndexed { index, message ->
            val next = if (index > 0) messages[index - 1] else null
            val previous = if (index == messages.lastIndex) null else messages[index + 1]
            var dateItem: Message? = null

            var type = getMessageType(message, previous, true)
            if (type == ChatItemType.DATE) {
                dateItem = Message(id = dateId, created_at = message.created_at, viewType = type)
                type = getMessageType(message, previous, false)
                dateId--
            }

            list.add(
                message.transform(
                    type,
                    getParentMessage(message.parent_id),
                    checkIsLast(message, next)
                )
            )

            dateItem?.let {
                list.add(it)
            }
        }

        return list
    }

    private fun getUser() {
        if (myId == null) myId = userDao.getUser()?.id
    }

    private fun getMessageType(
        current: Message?,
        previous: Message?,
        withDate: Boolean
    ): ChatItemType {
        return when {
            withDate && isNextDate(current?.created_at, previous?.created_at) -> ChatItemType.DATE
            current?.sender_id != myId -> {
                if (current?.image == null) ChatItemType.USER_TEXT_MESSAGE
                else ChatItemType.USER_IMAGE_MESSAGE
            }
            else -> {
                if (current?.image == null) ChatItemType.MY_TEXT_MESSAGE
                else ChatItemType.MY_IMAGE_MESSAGE
            }
        }
    }

    private fun isNextDate(firstDate: String?, secondDate: String?): Boolean {
        return firstDate.getDateString() != secondDate.getDateString()
    }

    private fun getParentMessage(parentId: Int?): ParentMessage? {
        if (parentId == null) return null
        val message = originalList?.firstOrNull { it.id == parentId } ?: return null
        return ParentMessage(
            message.id,
            message.text,
            message.image
        )
    }

    private fun checkIsLast(current: Message?, next: Message?): Boolean {
        return current?.sender_id != next?.sender_id || (isNextDate(
            current?.created_at,
            next?.created_at
        ))
    }

    fun setTypingEvent(on: Boolean) {
        typingMode.postValue(if (on) ChatStatusType.TYPING else ChatStatusType.ONLINE)
    }

    suspend fun refreshMessages() {
        getChatMessages(currentUserId)
    }
}