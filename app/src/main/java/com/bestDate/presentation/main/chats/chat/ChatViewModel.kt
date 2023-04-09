package com.bestDate.presentation.main.chats.chat

import android.graphics.Bitmap
import androidx.lifecycle.asLiveData
import com.bestDate.data.extension.toByteArray
import com.bestDate.data.model.Meta
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.InvitationUseCase
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase,
    private val invitationUseCase: InvitationUseCase
) : BaseViewModel() {

    var invitations = invitationUseCase.invitations.asLiveData()
    var messages = chatUseCase.messages
    var typingMode = chatUseCase.typingMode
    var meta: Meta = Meta()

    private var _sendInvitationLiveData: LiveEvent<Boolean> = LiveEvent()
    var sendInvitationLiveData: LiveEvent<Boolean> = _sendInvitationLiveData

    private var _sendMessageLiveData: LiveEvent<Boolean> = LiveEvent()
    var sendMessageLiveData: LiveEvent<Boolean> = _sendMessageLiveData

    private var _translateLiveData: LiveEvent<String?> = LiveEvent()
    var translateLiveData: LiveEvent<String?> = _translateLiveData

    fun sendInvitation(userId: Int?, invitationId: Int) {
        doAsync {
            invitationUseCase.sendInvitation(userId, invitationId)
            _sendInvitationLiveData.postValue(true)
        }
    }

    fun sendTextMessage(userId: Int?, parentId: Int?, text: String) {
        doAsync {
            chatUseCase.sendTextMessage(userId, parentId, text)
            _sendMessageLiveData.postValue(true)
        }
    }

    fun sendImageMessage(userId: Int?, text: String?, image: Bitmap) {
        doAsync {
            chatUseCase.sendImageMessage(userId, text, image.toByteArray())
            _sendMessageLiveData.postValue(true)
        }
    }

    fun editMessage(messageId: Int?, text: String) {
        doAsync {
            chatUseCase.editMessage(messageId, text)
            _sendMessageLiveData.postValue(true)
        }
    }

    fun getChatMessages(userId: Int?) {
        doAsync {
            chatUseCase.getChatMessages(userId)
            meta = chatUseCase.meta
        }
    }

    fun loadNextPage() {
        doAsync {
            chatUseCase.getNextPage()
            meta = chatUseCase.meta
        }
    }

    fun deleteMessage(messageId: Int?) {
        doAsync {
            chatUseCase.deleteMessage(messageId)
        }
    }

    fun sendTypingEvent() {
        doAsync {
            chatUseCase.sendTypingEvent()
        }
    }

    fun translateText(text: String?, language: String?) {
        doAsync {
            chatUseCase.translate(text, language)
            _translateLiveData.postValue(chatUseCase.translatedText)
        }
    }

    fun clearMessages() {
        chatUseCase.clearChatData()
    }
}