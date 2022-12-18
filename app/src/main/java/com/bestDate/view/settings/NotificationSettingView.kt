package com.bestDate.view.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bestDate.data.model.SettingsType
import com.bestDate.databinding.ViewSettingsNotificationBinding
import com.bestDate.db.entity.NotificationSettings

class NotificationSettingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ViewSettingsNotificationBinding =
        ViewSettingsNotificationBinding.inflate(LayoutInflater.from(context), this)

    var checkAction: ((checked: Boolean, SettingsType) -> Unit)? = null

    init {
        with(binding) {
            likesSwitch.checkAction = {
                checkAction?.invoke(it, SettingsType.NOTIFY_LIKES)
            }
            matchesSwitch.checkAction = {
                checkAction?.invoke(it, SettingsType.NOTIFY_MATCHES)
            }
            invitationsSwitch.checkAction = {
                checkAction?.invoke(it, SettingsType.NOTIFY_INVITATION)
            }
            messagesSwitch.checkAction = {
                checkAction?.invoke(it, SettingsType.NOTIFY_MESSAGES)
            }
            guestsSwitch.checkAction = {
                checkAction?.invoke(it, SettingsType.NOTIFY_GUESTS)
            }
        }
    }

    fun setNotificationSettings(settings: NotificationSettings) {
        with(binding) {
            likesSwitch.setChecked(settings.likes)
            matchesSwitch.setChecked(settings.matches)
            invitationsSwitch.setChecked(settings.invitations)
            messagesSwitch.setChecked(settings.messages)
            guestsSwitch.setChecked(settings.guests)
        }
    }
}