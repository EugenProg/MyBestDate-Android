package com.bestDate.presentation.main.chats.chat

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.Message
import com.bestDate.network.remote.ChatsRemoteData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatUseCase @Inject constructor(
    private val chatsRemoteData: ChatsRemoteData
) {
    var messages: MutableLiveData<MutableList<Message>?> = MutableLiveData(mutableListOf())

    suspend fun sendTextMessage(recipientId: Int?, parentId: Int?, text: String) {
        val response = chatsRemoteData.sendTextMessage(recipientId.orZero, parentId, text)
        if (response.isSuccessful) {

        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun sendImageMessage(recipientId: Int?, parentId: Int?, text: String?, image: ByteArray) {
        val requestFile =
            image.toRequestBody("multipart/form-data".toMediaTypeOrNull(), 0, image.size)
        val body = MultipartBody.Part.createFormData("media", "name", requestFile)
        val response = chatsRemoteData.sendImageMessage(recipientId.orZero, parentId, body)
        if (response.isSuccessful) {
            if (!text.isNullOrBlank()) {
                val messageId = response.body()?.data?.id.orZero
                chatsRemoteData.updateMessage(messageId, text)
            }
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun editMessage(messageId: Int?, text: String) {
        val response = chatsRemoteData.updateMessage(messageId.orZero, text)
        if (response.isSuccessful) {

        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun deleteMessage(messageId: Int?) {
        val response = chatsRemoteData.deleteMessage(messageId.orZero)
        if (response.isSuccessful) {

        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun sendTypingEvent(recipientId: Int?) {
        val response = chatsRemoteData.sendTypingEvent(recipientId.orZero)
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
        val response = chatsRemoteData.getChatMessages(userId.orZero)
        if (response.isSuccessful) {
            response.body()?.let {
                messages.postValue(it.data)
            }
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }
}