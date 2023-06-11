package com.bestDate.data.utils.notifications

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getPushType
import com.bestDate.data.model.ShortUserData
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.data.utils.Logger
import com.bestDate.db.dao.UserDao
import com.bestDate.view.alerts.showInvitationPush
import com.bestDate.view.alerts.showLikePush
import com.bestDate.view.alerts.showMatchPush
import com.bestDate.view.alerts.showModerationFailedPush
import com.bestDate.view.alerts.showModerationSuccessPush
import com.bestDate.view.button.InvitationActions
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
        when (notificationType) {
            NotificationType.LIKE -> activity.showLikePush(user) {
                _navigationAction.postValue(Pair(notificationType.destination, null))
            }
            NotificationType.MATCH -> activity.showMatchPush(user) {
                _navigationAction.postValue(Pair(notificationType.destination, null))
            }
            NotificationType.INVITATION -> activity.showInvitationPush(user) {
                _navigationAction.postValue(
                    Pair(
                        notificationType.destination,
                        createInvitationBundle(InvitationActions.NEW)
                    )
                )
            }
            NotificationType.INVITATION_ANSWER -> activity.showInvitationPush(user) {
                _navigationAction.postValue(
                    Pair(
                        notificationType.destination,
                        createInvitationBundle(InvitationActions.SENT)
                    )
                )
            }

            NotificationType.MODERATION_SUCCESS -> activity.showModerationSuccessPush(title, body) {
                _navigationAction.postValue(Pair(notificationType.destination, null))
            }

            NotificationType.MODERATION_FAILED -> activity.showModerationFailedPush(title, body) {
                _navigationAction.postValue(
                    Pair(
                        notificationType.destination,
                        bundleOf("show_photo_select" to true)
                    )
                )
            }
        }
    }

    private fun createInvitationBundle(type: InvitationActions) =
        bundleOf("page" to type)

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

    @Provides
    @Singleton
    fun providePusherCenter(userDao: UserDao, preferencesUtils: PreferencesUtils) =
        PusherCenter(userDao, preferencesUtils)
}