package com.bestDate.network.services

import com.bestDate.data.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ChatsService {
    /**Get chat list*/
    @GET("/api/v1/chats")
    suspend fun getChatList(): Response<ChatListResponse>

    /**Get user chat messages*/
    @GET("/api/v1/chat/{id}")
    suspend fun getChatMessages(@Path("id") id: Int): Response<ChatMessagesResponse>

    /**Delete chat*/
    @DELETE("/api/v1/chat/{id}")
    suspend fun deleteChat(@Path("id") id: Int): Response<BaseResponse>

    /**Send text message with parent*/
    @POST("/api/v1/message/{recipient}/{parent}")
    suspend fun sendTextMessageWithParent(
        @Path("recipient") recipientId: Int,
        @Path("parent") parentId: Int
    ): Response<SendMessageResponse>

    /**Send text message without parent*/
    @POST("/api/v1/message/{recipient}")
    suspend fun sendTextMessageWithoutParent(@Path("recipient") recipientId: Int):
            Response<SendMessageResponse>

    /**Send image message with parent*/
    @Multipart
    @POST("/api/v1/message/{recipient}/{parent}")
    suspend fun sendImageMessageWithParent(
        @Path("recipient") recipientId: Int,
        @Path("parent") parentId: Int,
        @Part image: MultipartBody.Part
    ): Response<SendMessageResponse>

    /**Send image message without parent*/
    @Multipart
    @POST("/api/v1/message/{recipient}")
    suspend fun sendImageMessageWithoutParent(
        @Path("recipient") recipientId: Int,
        @Part image: MultipartBody.Part
    ): Response<SendMessageResponse>

    /**Update message*/
    @PUT("/api/v1/message/{message}")
    suspend fun updateMessage(
        @Path("message") messageId: Int,
        @Body request: UpdateMessageRequest
    ): Response<SendMessageResponse>

    /**Delete message*/
    @DELETE("/api/v1/message/{message}")
    suspend fun deleteMessage(@Path("message") messageId: Int): Response<BaseResponse>

    /**Send typing event*/
    @POST("/api/v1/chat-typing-event/{recipient}")
    suspend fun sendTypingEvent(@Path("recipient") recipientId: Int): Response<BaseResponse>

    /**Send chat read event*/
    @POST("/api/v1/chat-read-event/{recipient}")
    suspend fun sendReadingEvent(@Path("recipient") recipientId: Int): Response<BaseResponse>
}