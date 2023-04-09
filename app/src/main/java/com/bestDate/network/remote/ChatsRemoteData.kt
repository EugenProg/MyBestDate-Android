package com.bestDate.network.remote

import com.bestDate.data.model.SendMessageRequest
import com.bestDate.network.services.ChatsService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ChatsRemoteData @Inject constructor(
    private val service: ChatsService
) {
    suspend fun getChatList(page: Int) = service.getChatList(page)

    suspend fun getChatMessages(id: Int, page: Int) = service.getChatMessages(id, page)

    suspend fun deleteChat(id: Int) = service.deleteChat(id)

    suspend fun sendTextMessage(recipientId: Int, parentId: Int?, text: String) =
        if (parentId == null) service.sendTextMessageWithoutParent(
            recipientId,
            SendMessageRequest(text)
        )
        else service.sendTextMessageWithParent(recipientId, parentId, SendMessageRequest(text))

    suspend fun sendImageMessage(recipientId: Int, image: MultipartBody.Part, text: String?) =
        if (text.isNullOrBlank()) {
            service.sendImageMessageWithoutText(recipientId, image)
        } else {
            val body = text.toRequestBody("text".toMediaTypeOrNull())
            service.sendImageMessageWithText(recipientId, image, body)
        }

    suspend fun updateMessage(messageId: Int, text: String) =
        service.updateMessage(messageId, SendMessageRequest(text))

    suspend fun deleteMessage(messageId: Int) = service.deleteMessage(messageId)

    suspend fun sendTypingEvent(recipientId: Int) = service.sendTypingEvent(recipientId)

    suspend fun sendReadingEvent(recipientId: Int) = service.sendReadingEvent(recipientId)
}