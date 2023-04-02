package com.bestDate.data.utils.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.data.model.*
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.data.utils.Logger
import com.bestDate.db.dao.UserDao
import com.bestDate.network.NetworkModule
import com.google.gson.Gson
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import com.pusher.client.channel.PrivateChannelEventListener
import com.pusher.client.channel.PusherEvent
import com.pusher.client.connection.ConnectionState
import com.pusher.client.util.HttpAuthorizer
import javax.inject.Inject
import kotlin.Exception

class PusherCenter @Inject constructor(
    private var userDao: UserDao,
    private var preferencesUtils: PreferencesUtils
) {
    private var chatChannelName: String = ""
    private var userChannelName: String = ""
    private var pusher: Pusher? = null
    private var chatChannel: Channel? = null
    private var userChannel: Channel? = null

    private var _newMessageLiveData: MutableLiveData<Message?> = MutableLiveData()
    var newMessageLiveData: LiveData<Message?> = _newMessageLiveData

    private var _editMessageLiveData: MutableLiveData<Message?> = MutableLiveData()
    var editMessageLiveData: LiveData<Message?> = _editMessageLiveData

    private var _deleteMessageLiveData: MutableLiveData<Message?> = MutableLiveData()
    var deleteMessageLiveData: LiveData<Message?> = _deleteMessageLiveData

    private var _coinsLiveData: MutableLiveData<String?> = MutableLiveData()
    var coinsLiveData: LiveData<String?> = _coinsLiveData

    private var _readingLiveData: MutableLiveData<Message?> = MutableLiveData()
    var readingLiveData: LiveData<Message?> = _readingLiveData

    private var _typingLiveData: MutableLiveData<Int?> = MutableLiveData()
    var typingLiveData: LiveData<Int?> = _typingLiveData

    fun startPusher() {
        if (!isConnected()) {
            userDao.getUser()?.id?.let {
                chatChannelName = "private-chat.$it"
                userChannelName = "private-user.$it"
            }

            createConnection()
            chatChannel = pusher?.subscribePrivate(
                chatChannelName, chatListener(), PusherEventType.MESSAGE.serverName,
                PusherEventType.UPDATE.serverName, PusherEventType.DELETE.serverName,
                PusherEventType.READ.serverName, PusherEventType.TYPING.serverName
            )
            userChannel = pusher?.subscribePrivate(
                userChannelName, userListener(), PusherEventType.COINS.serverName
            )
        }
    }

    private fun createConnection() {
        val authorizer = HttpAuthorizer("${NetworkModule.providesCoreBaseURL()}/broadcasting/auth")
        authorizer.setHeaders(
            hashMapOf(Pair("Authorization", preferencesUtils.getString(Preferences.ACCESS_TOKEN))
            )
        )
        val options = PusherOptions()
            .setCluster("eu")
            .setAuthorizer(authorizer)

        pusher = Pusher("d995b7146329bb8422f3", options)
        pusher?.connect()
    }

    private fun chatListener(): PrivateChannelEventListener {
        return object : PrivateChannelEventListener {
            override fun onEvent(event: PusherEvent?) {
                doAction(getEventType(event?.eventName), event?.data)
            }

            override fun onSubscriptionSucceeded(channelName: String?) {
                Logger.print("Chat channel binding successful")
            }

            override fun onAuthenticationFailure(message: String?, e: Exception?) {
                Logger.print("Chat channel binding failure by reason: ${e?.message}")
            }
        }
    }

    private fun userListener(): PrivateChannelEventListener {
        return object : PrivateChannelEventListener {
            override fun onEvent(event: PusherEvent?) {
                doAction(getEventType(event?.eventName), event?.data)
            }

            override fun onSubscriptionSucceeded(channelName: String?) {
                Logger.print("User channel binding successful")
            }

            override fun onAuthenticationFailure(message: String?, e: Exception?) {
                Logger.print("User channel binding failure by reason: ${e?.message}")
            }
        }
    }

    private fun doAction(eventType: PusherEventType, data: String?) {
        when(eventType) {
            PusherEventType.MESSAGE -> _newMessageLiveData.postValue(parseMessageData(data))
            PusherEventType.UPDATE -> _editMessageLiveData.postValue(parseMessageData(data))
            PusherEventType.DELETE -> _deleteMessageLiveData.postValue(parseMessageData(data))
            PusherEventType.COINS -> _coinsLiveData.postValue(parseCoinsData(data))
            PusherEventType.READ -> _readingLiveData.postValue(parseReadingData(data))
            PusherEventType.TYPING -> _typingLiveData.postValue(parseTypingData(data))
            PusherEventType.UNKNOWN -> {
                Logger.print("Unknown pusher event with this data: $data")
            }
        }
    }

    private fun parseMessageData(data: String?): Message? {
        return try {
            Gson().fromJson(data, PusherMessageResponse::class.java).message
        } catch (e: Exception) {
            Logger.print("Message parsing error: ${e.message}")
            null
        }
    }

    private fun parseCoinsData(data: String?): String? {
        return try {
            Gson().fromJson(data, PusherCoinsResponse::class.java).coins
        } catch (e: Exception) {
            Logger.print("Coins parsing error: ${e.message}")
            null
        }
    }

    private fun parseTypingData(data: String?): Int? {
        return try {
            Gson().fromJson(data, PusherTypingResponse::class.java).sender_id
        } catch (e: Exception) {
            Logger.print("Typing parsing error: ${e.message}")
            null
        }
    }

    private fun parseReadingData(data: String?): Message? {
        return try {
            Gson().fromJson(data, PusherReadingResponse::class.java).last_message
        } catch (e: Exception) {
            Logger.print("Reading parsing error: ${e.message}")
            null
        }
    }

    private fun isConnected(): Boolean = pusher?.connection?.state == ConnectionState.CONNECTED

    fun disconnect() {
        pusher?.unsubscribe(chatChannelName)
        pusher?.unsubscribe(userChannelName)
        pusher?.disconnect()
        Logger.print(">>> Pusher is disconnected")
    }

    private fun getEventType(eventName: String?): PusherEventType {
        return PusherEventType.values().firstOrNull { it.serverName == eventName }
            ?: PusherEventType.UNKNOWN
    }
}

enum class PusherEventType(var serverName: String) {
    MESSAGE("private-message"),
    UPDATE("private-update"),
    DELETE("private-delete"),
    READ("private-read"),
    TYPING("private-typing"),
    COINS("private-coins"),
    UNKNOWN("")
}