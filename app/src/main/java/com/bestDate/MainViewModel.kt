package com.bestDate

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.bestDate.data.utils.SessionManager
import com.bestDate.data.utils.notifications.NotificationCenter
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.main.chats.ChatListUseCase
import com.bestDate.presentation.main.chats.chat.ChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val chatListUseCase: ChatListUseCase,
    private val chatUseCase: ChatUseCase,
    private val userUseCase: UserUseCase,
    private val notificationCenter: NotificationCenter,
    sessionManager: SessionManager
) : BaseViewModel() {

    val myUser = userUseCase.getMyUser.asLiveData()
    val hasNewChats = chatListUseCase.hasNewChats
    var loggedOut = sessionManager.loggedOut
    val notificationsAction = notificationCenter.notificationsAction
    val navigationAction = notificationCenter.navigationAction

    fun refreshChatList() {
        doAsync {
            chatListUseCase.refreshChatList()
        }
    }

    fun refreshMessageList() {
        doAsync {
            chatUseCase.getChatMessages(notificationCenter.getUserId())
        }
    }

    fun clearUserData() {
        userUseCase.clearUserData()
    }

    fun showPush(activity: AppCompatActivity) {
        notificationCenter.showPush(activity)
    }

    fun isCurrentUserChat(): Boolean {
        return chatUseCase.isCurrentUserChat(notificationCenter.getUserId())
    }
}