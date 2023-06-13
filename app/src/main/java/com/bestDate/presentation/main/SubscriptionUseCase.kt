package com.bestDate.presentation.main

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.orZero
import com.bestDate.data.extension.toLongServerDate
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.SubscriptionInfoRequest
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.network.remote.SubscriptionRemoteData
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionUseCase @Inject constructor(
    private val remoteData: SubscriptionRemoteData,
    private val preferencesUtils: PreferencesUtils
) {

    suspend fun getAppSettings() {
        val response = remoteData.getAppSettings()
        if (response.isSuccessful) {
            response.body()?.data?.let {
                preferencesUtils.saveBoolean(Preferences.SUBSCRIPTION_MODE_ENABLED, it.subscription == true)
                preferencesUtils.saveInt(Preferences.FREE_MESSAGES_COUNT, it.free_messages_count.orZero)
                preferencesUtils.saveInt(Preferences.FREE_INVITATIONS_COUNT, it.free_invitations_count.orZero)
                preferencesUtils.saveInt(Preferences.FREE_MATCHES_COUNT, it.free_matches_count.orZero)
                preferencesUtils.saveInt(Preferences.ADDITIONAL_MESSAGE_PRICE, it.additional_messages_rate.orZero)
                preferencesUtils.saveInt(Preferences.ADDITIONAL_INVITATION_PRICE, it.additional_invitations_rate.orZero)
            }
        }
    }

    suspend fun getUserSubscriptionInfo() {
        val androidEndDate = getAndroidSubscription()
        val iosEndDate = getIosSubscription()
        val endDate = when {
            androidEndDate.isNullOrBlank() && iosEndDate.isNullOrBlank() -> null
            androidEndDate.isNullOrBlank() && !iosEndDate.isNullOrBlank() -> iosEndDate
            !androidEndDate.isNullOrBlank() && iosEndDate.isNullOrBlank() -> androidEndDate
            else -> {
                if (androidEndDate.orEmpty() > iosEndDate.orEmpty()) androidEndDate
                else iosEndDate
            }
        }
        endDate?.let {
            preferencesUtils.saveString(Preferences.ACTIVE_SUBSCRIPTION_END, it)
        }

    }

    private suspend fun getAndroidSubscription(): String? {
        val response = remoteData.getUserSubscriptionInfo(SubscriptionInfoRequest())
        if (response.isSuccessful) {
            val hasAActiveSubscription = response.body()?.data?.end_at.orEmpty() > Date().toLongServerDate()
            preferencesUtils.saveBoolean(Preferences.HAS_A_ACTIVE_ANDROID_SUBSCRIPTION, hasAActiveSubscription)
            return if (hasAActiveSubscription) response.body()?.data?.end_at.orEmpty() else null
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    private suspend fun getIosSubscription(): String? {
        val response = remoteData.getUserSubscriptionInfo(SubscriptionInfoRequest(device = "ios"))
        if (response.isSuccessful) {
            val hasAActiveSubscription = response.body()?.data?.end_at.orEmpty() > Date().toLongServerDate()
            preferencesUtils.saveBoolean(Preferences.HAS_A_ACTIVE_IOS_SUBSCRIPTION, hasAActiveSubscription)
            return if (hasAActiveSubscription) response.body()?.data?.end_at.orEmpty() else null
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun updateSubscriptionInfo(startDate: String, endDate: String) {
        val response = remoteData.updateSubscriptionInfo(startDate, endDate)
        if (!response.isSuccessful)
            throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }
}