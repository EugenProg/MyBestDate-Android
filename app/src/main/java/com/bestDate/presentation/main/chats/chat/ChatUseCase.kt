package com.bestDate.presentation.main.chats.chat

import com.bestDate.network.remote.ChatsRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatUseCase @Inject constructor(
    private val chatsRemoteData: ChatsRemoteData
) {
}