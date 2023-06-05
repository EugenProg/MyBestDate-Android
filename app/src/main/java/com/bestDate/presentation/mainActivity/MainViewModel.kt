package com.bestDate.presentation.mainActivity

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.bestDate.data.model.Message
import com.bestDate.data.utils.SessionManager
import com.bestDate.data.utils.notifications.NotificationCenter
import com.bestDate.data.utils.notifications.PusherCenter
import com.bestDate.presentation.auth.AuthUseCase
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.InvitationUseCase
import com.bestDate.presentation.main.SubscriptionUseCase
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.main.chats.ChatListUseCase
import com.bestDate.presentation.main.chats.chat.ChatUseCase
import com.bestDate.presentation.main.guests.GuestsUseCase
import com.bestDate.presentation.main.userProfile.settings.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val chatListUseCase: ChatListUseCase,
    private val chatUseCase: ChatUseCase,
    private val userUseCase: UserUseCase,
    private val authUseCase: AuthUseCase,
    private val invitationUseCase: InvitationUseCase,
    private val guestsUseCase: GuestsUseCase,
    private val notificationCenter: NotificationCenter,
    private val pusherCenter: PusherCenter,
    private val subscriptionUseCase: SubscriptionUseCase,
    private val settingsUseCase: SettingsUseCase,
    sessionManager: SessionManager
) : BaseViewModel() {

    val myUser = userUseCase.getMyUser.asLiveData()
    val hasNewChats = chatListUseCase.hasNewChats
    val hasNewGuests = guestsUseCase.hasNewGuests
    var loggedOut = sessionManager.loggedOut
    val notificationsAction = notificationCenter.notificationsAction
    val navigationAction = notificationCenter.navigationAction
    val deleteMessageLiveData = pusherCenter.deleteMessageLiveData
    val editMessageLiveData = pusherCenter.editMessageLiveData
    val newMessageLiveData = pusherCenter.newMessageLiveData
    val readingLiveData = pusherCenter.readingLiveData
    val typingLiveData = pusherCenter.typingLiveData
    val coinsLiveData = pusherCenter.coinsLiveData

    fun refreshChatList() {
        doAsync {
            chatListUseCase.refreshChatList()
        }
    }

    fun setChatListTypingEvent(senderId: Int?, isOn: Boolean) {
        doAsync {
            chatListUseCase.setTypingEvent(senderId, isOn)
        }
    }

    fun setChatTypingEvent(isOn: Boolean) {
        doAsync {
            chatUseCase.setTypingEvent(isOn)
        }
    }

    fun clearUserData() {
        userUseCase.clearUserData()
    }

    fun showPush(activity: AppCompatActivity) {
        notificationCenter.showPush(activity)
    }

    fun isCurrentUserChat(senderId: Int?): Boolean {
        return chatUseCase.isCurrentUserChat(senderId)
    }

    fun isCurrentUserChat(): Boolean {
        return chatUseCase.isCurrentUserChat(notificationCenter.getUserId())
    }

    fun refreshData(appLanguage: String) {
        if (authUseCase.tokenIsFresh) {
            doAsync {
                userUseCase.changeLanguage(appLanguage)
                invitationUseCase.refreshInvitations()
                subscriptionUseCase.getUserSubscriptionInfo()
                pusherCenter.startPusher()
                settingsUseCase.refreshUserSettings()
            }
        }
    }

    fun disconnectPusher() {
        if (authUseCase.tokenIsFresh) {
            pusherCenter.disconnect()
        }
    }

    fun setCoinsCount(coins: String?) {
        userUseCase.coinsCount.postValue(coins)
    }

    fun sendReadingEvent(senderId: Int?) {
        doAsync {
            chatUseCase.sendReadingEvent(senderId)
        }
    }

    fun addChatMessage(message: Message?) {
        doAsync {
            chatUseCase.addPusherMessage(message)
        }
    }

    fun editChatMessage(message: Message?) {
        doAsync {
            chatUseCase.editPusherMessage(message)
        }
    }

    fun deleteChatMessage(message: Message?) {
        doAsync {
            chatUseCase.deletePusherMessage(message)
        }
    }

    fun refreshChatMessages() {
        doAsync {
            chatUseCase.refreshMessages()
        }
    }

    fun setMessageToChatList(message: Message?) {
        doAsync {
            chatListUseCase.setMessage(message)
        }
    }

    fun refreshAppSettings() {
        doAsync {
            subscriptionUseCase.getAppSettings()
        }
    }

    fun updateSubscriptionInfo(start: String, end: String) {
        doAsync {
            subscriptionUseCase.updateSubscriptionInfo(start, end)
        }
    }
}