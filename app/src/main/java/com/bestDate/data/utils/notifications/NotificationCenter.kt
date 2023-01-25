package com.bestDate.data.utils.notifications

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getPushType
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.ShortUserData
import com.bestDate.data.utils.Logger
import com.bestDate.view.alerts.showDefaultPush
import com.bestDate.view.alerts.showInvitationPush
import com.bestDate.view.alerts.showLikePush
import com.bestDate.view.alerts.showMatchPush
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

class NotificationCenter @Inject constructor() {

    private var _notificationsAction: MutableLiveData<NotificationType> = MutableLiveData()
    var notificationsAction: LiveData<NotificationType> = _notificationsAction

    private var _navigationAction: MutableLiveData<Pair<Int, Bundle?>> = MutableLiveData()
    var navigationAction: LiveData<Pair<Int, Bundle?>> = _navigationAction

    private var user: ShortUserData? = null
    private var notificationType: NotificationType = NotificationType.DEFAULT_PUSH
    private var title: String? = null
    private var body: String? = null

    fun setNotification(title: String?, body: String?, type: String?, userData: String?) {
        notificationType = type?.getPushType() ?: NotificationType.DEFAULT_PUSH
        user = getUser(userData)
        this.title = title
        this.body = body
        _notificationsAction.postValue(notificationType)
    }

    fun showPush(activity: AppCompatActivity) {
        when(notificationType) {
            NotificationType.LIKE -> activity.showLikePush(user) {
                _navigationAction.postValue(Pair(notificationType.destination, null))
            }
            NotificationType.MATCH -> activity.showMatchPush(user) {
                _navigationAction.postValue(Pair(notificationType.destination, null))
            }
            NotificationType.INVITATION -> activity.showInvitationPush(user) {
                _navigationAction.postValue(Pair(notificationType.destination, null))
            }
            else -> {
                activity.showDefaultPush(title, body) {
                    val bundle =
                        if (notificationType == NotificationType.MESSAGE) createMessageBundle()
                        else null
                    _navigationAction.postValue(Pair(notificationType.destination, bundle))
                }
            }
        }
    }

    private fun createMessageBundle(): Bundle =
        bundleOf("user" to user,
            "backScreen" to BackScreenType.PROFILE)

    private fun getUser(user: String?): ShortUserData? {
        if (user.isNullOrBlank()) return null
        try {
            return Gson().fromJson(user, ShortUserData::class.java)
        } catch (e: Exception) {
            Logger.print("Parsing exception: ${e.message}")
        }
        return null
    }

    fun getUserId(): Int? = user?.id
}

@Module
@InstallIn(SingletonComponent::class)
class NotificationCenterModule {
    @Provides
    @Singleton
    fun provideNotificationCenter() = NotificationCenter()
}