package com.bestDate.presentation.main

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.InternalException
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.network.remote.SubscriptionRemoteData
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
            }
        }
    }

    suspend fun getUserSubscriptionInfo() {
        val response = remoteData.getUserSubscriptionInfo()
        if (!response.isSuccessful)
            throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun updateSubscriptionInfo(startDate: String, endDate: String) {
        val response = remoteData.updateSubscriptionInfo(startDate, endDate)
        if (!response.isSuccessful)
            throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }
}