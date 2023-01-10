package com.bestDate.network.remote

import com.bestDate.data.model.UpdateMessageRequest
import com.bestDate.network.services.ChatsService
import okhttp3.MultipartBody
import javax.inject.Inject

class ChatsRemoteData @Inject constructor(
    private val service: ChatsService
) {
    suspend fun getChatList() = service.getChatList()

    suspend fun getChatMessages(id: Int) = service.getChatMessages(id)

    suspend fun deleteChat(id: Int) = service.deleteChat(id)

    suspend fun sendTextMessage(recipientId: Int, parentId: Int?) =
        if (parentId == null) service.sendTextMessageWithoutParent(recipientId)
        else service.sendTextMessageWithParent(recipientId, parentId)

    suspend fun sendImageMessage(recipientId: Int, parentId: Int?, image: MultipartBody.Part) =
        if (parentId == null) service.sendImageMessageWithoutParent(recipientId, image)
        else service.sendImageMessageWithParent(recipientId, parentId, image)

    suspend fun updateMessage(messageId: Int, text: String) =
        service.updateMessage(messageId, UpdateMessageRequest(text))

    suspend fun deleteMessage(messageId: Int) = service.deleteMessage(messageId)

    suspend fun sendTypingEvent(recipientId: Int) = service.sendTypingEvent(recipientId)

    suspend fun sendReadingEvent(recipientId: Int) = service.sendReadingEvent(recipientId)
}