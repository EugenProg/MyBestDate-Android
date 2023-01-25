package com.bestDate.presentation.main.chats.chat

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.data.extension.toByteArray
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.InvitationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase,
    private val invitationUseCase: InvitationUseCase
) : BaseViewModel() {

    var invitations = invitationUseCase.invitations.asLiveData()
    var messages = chatUseCase.messages

    private var _sendInvitationLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var sendInvitationLiveData: LiveData<Boolean> = _sendInvitationLiveData

    private var _sendMessageLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var sendMessageLiveData: LiveData<Boolean> = _sendMessageLiveData

    private var _translateLiveData: MutableLiveData<String?> = MutableLiveData()
    var translateLiveData: LiveData<String?> = _translateLiveData

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
        }
    }

    fun deleteMessage(messageId: Int?) {
        doAsync {
            chatUseCase.deleteMessage(messageId)
        }
    }

    fun sendTypingEvent(recipientId: Int?) {
        doAsync {
            chatUseCase.sendTypingEvent(recipientId)
        }
    }

    fun sendReadingEvent(recipientId: Int?) {
        doAsync {
            chatUseCase.sendReadingEvent(recipientId)
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