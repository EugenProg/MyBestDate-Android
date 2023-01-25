package com.bestDate.data.utils.notifications

import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {
    @Inject
    lateinit var preferencesUtils: PreferencesUtils

    @Inject
    lateinit var notificationCenter: NotificationCenter

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        preferencesUtils.saveString(Preferences.FIREBASE_TOKEN, token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val type = message.data["type"]
        val body = message.data["message_text"]
        val title = message.data["title"]
        val user = message.data["user"]
        notificationCenter.setNotification(title, body, type, user)
    }
}