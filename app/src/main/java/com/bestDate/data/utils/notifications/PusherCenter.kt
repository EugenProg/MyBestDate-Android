package com.bestDate.data.utils.notifications

import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.data.utils.Logger
import com.bestDate.db.dao.UserDao
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import com.pusher.client.connection.ConnectionState
import com.pusher.client.util.HttpAuthorizer
import javax.inject.Inject

class PusherCenter @Inject constructor(
    private var userDao: UserDao,
    private var preferencesUtils: PreferencesUtils
) {
    private var chatChannelName: String = ""
    private var userChannelName: String = ""
    private var pusher: Pusher? = null
    private var chatChannel: Channel? = null
    private var userChannel: Channel? = null

    fun startPusher() {
        if (!isConnected()) {
            userDao.getUser()?.id?.let {
                chatChannelName = "private-chat.$it"
                userChannelName = "private-user.$it"
            }

            createConnection()
            chatChannel = pusher?.subscribePrivate(chatChannelName)
            userChannel = pusher?.subscribePrivate(userChannelName)
            setListeners()
        }
    }

    private fun createConnection() {
        val authorizer = HttpAuthorizer("https://dev-api.bestdate.info/broadcasting/auth")
        authorizer.setHeaders(
            hashMapOf(
                Pair(
                    "Authorization",
                    preferencesUtils.getString(Preferences.ACCESS_TOKEN)
                )
            )
        )
        val options = PusherOptions()
            .setCluster("eu")
            .setAuthorizer(authorizer)

        pusher = Pusher("d995b7146329bb8422f3", options)
        pusher?.connect()
    }

    private fun setListeners() {
        messageListener()
        editListener()
        readListener()
        deleteListener()
        typingListener()
        coinsListener()
    }

    private fun messageListener() {
        chatChannel?.bind("private-message") {

        }
    }

    private fun editListener() {
        chatChannel?.bind("private-update") {

        }
    }

    private fun readListener() {
        chatChannel?.bind("private-read") {

        }
    }

    private fun deleteListener() {
        chatChannel?.bind("private-delete") {

        }
    }

    private fun typingListener() {
        chatChannel?.bind("private-typing") {

        }
    }

    private fun coinsListener() {
        userChannel?.bind("private-coins") {

        }
    }

    private fun isConnected(): Boolean = pusher?.connection?.state == ConnectionState.CONNECTED

    private fun closePusherConnection() {
        pusher?.unsubscribe(chatChannelName)
        pusher?.unsubscribe(userChannelName)
        pusher?.disconnect()
        Logger.print(">>> Pusher is disconnected")
    }
}