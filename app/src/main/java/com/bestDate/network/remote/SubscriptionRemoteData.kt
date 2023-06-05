package com.bestDate.network.remote

import com.bestDate.data.model.SubscriptionInfoRequest
import com.bestDate.network.services.SubscriptionService
import javax.inject.Inject

class SubscriptionRemoteData @Inject constructor(
    private val service: SubscriptionService
) {
    suspend fun getAppSettings() = service.getAppSettings()

    suspend fun getUserSubscriptionInfo() =
        service.getUserSubscriptionInfo(SubscriptionInfoRequest(device = "ios"))

    suspend fun updateSubscriptionInfo(startDate: String, endDate: String) =
        service.updateSubscriptionInfo(
            SubscriptionInfoRequest(start_at = startDate, end_at = endDate)
        )
}