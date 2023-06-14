package com.bestDate.data.utils.subscription

import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import javax.inject.Inject

class SubscriptionUtil @Inject constructor(
    private val preferencesUtils: PreferencesUtils
) {

    private fun hasAActiveSubscription() =
        preferencesUtils.getBoolean(Preferences.HAS_A_ACTIVE_SUBSCRIPTION) ||
                preferencesUtils.getBoolean(Preferences.HAS_A_ACTIVE_IOS_SUBSCRIPTION) ||
                preferencesUtils.getBoolean(Preferences.HAS_A_ACTIVE_ANDROID_SUBSCRIPTION)

    fun invitationSendAllowed() =
        if (
            preferencesUtils.getBoolean(Preferences.IS_A_MAN) &&
            preferencesUtils.getBoolean(Preferences.SUBSCRIPTION_MODE_ENABLED) &&
            !hasAActiveSubscription()
        ) {
            preferencesUtils.getInt(Preferences.SENT_INVITATIONS_TODAY) <
                    preferencesUtils.getInt(Preferences.FREE_INVITATIONS_COUNT)
        } else true

    fun messageSendAllowed() =
        if (
            preferencesUtils.getBoolean(Preferences.IS_A_MAN) &&
            preferencesUtils.getBoolean(Preferences.SUBSCRIPTION_MODE_ENABLED) &&
            !hasAActiveSubscription()
        ) {
            preferencesUtils.getInt(Preferences.SENT_MESSAGES_TODAY) <
                    preferencesUtils.getInt(Preferences.FREE_MESSAGES_COUNT)
        } else true

    fun hideGuests() = preferencesUtils.getBoolean(Preferences.IS_A_MAN) &&
            preferencesUtils.getBoolean(Preferences.SUBSCRIPTION_MODE_ENABLED) &&
            !hasAActiveSubscription()

    fun getAdditionInvitationPrice(): Int {
        return preferencesUtils.getInt(Preferences.ADDITIONAL_INVITATION_PRICE)
    }

    fun getAdditionMessagePrice(): Int {
        return preferencesUtils.getInt(Preferences.ADDITIONAL_MESSAGE_PRICE)
    }

    fun getSubscriptionEndDate(): String? {
        return if (hasAActiveSubscription()) preferencesUtils.getString(Preferences.ACTIVE_SUBSCRIPTION_END)
        else null
    }
}