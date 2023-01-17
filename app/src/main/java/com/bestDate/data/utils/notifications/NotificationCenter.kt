package com.bestDate.data.utils.notifications

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import com.bestDate.MainActivity
import com.bestDate.R
import com.bestDate.data.extension.getPushType
import com.bestDate.data.model.ShortUserData
import com.bestDate.data.utils.Logger
import com.bestDate.view.alerts.showDefaultDialog
import com.bestDate.view.alerts.showLikePush
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.typeOf

class NotificationCenter @Inject constructor() {

    private var _notificationsAction: MutableLiveData<Boolean> = MutableLiveData()
    var notificationsAction: LiveData<Boolean> = _notificationsAction

    private var _navigationAction: MutableLiveData<Int> = MutableLiveData()
    var navigationAction: LiveData<Int> = _navigationAction

    private var user: ShortUserData? = null
    private var notificationType: NotificationType = NotificationType.DEFAULT_PUSH
    private var title: String? = null
    private var body: String? = null

    fun setNotification(title: String?, body: String?, type: String?, userData: String?) {
        notificationType = type?.getPushType() ?: NotificationType.DEFAULT_PUSH
        user = getUser(userData)
        this.title = title
        this.body = body
        _notificationsAction.postValue(true)
    }

    fun showPush(activity: AppCompatActivity) {
        when(notificationType) {
            NotificationType.LIKE -> activity.showLikePush(user) {
                _navigationAction.postValue(notificationType.destination)
            }
        }
    }

    private fun getUser(user: String?): ShortUserData? {
        if (user.isNullOrBlank()) return null
        try {
            return Gson().fromJson(user, ShortUserData::class.java)
        } catch (e: Exception) {
            Logger.print("Parsing exception: ${e.message}")
        }
        return null
    }

    private fun createPendingIntent(context: Context, destinationId: Int, args: Bundle?): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.routes)
            .setDestination(destinationId)
            .setArguments(args)
            .createPendingIntent()
    }
}

@Module
@InstallIn(SingletonComponent::class)
class NotificationCenterModule {
    @Provides
    @Singleton
    fun provideNotificationCenter() = NotificationCenter()
}