package com.bestDate.network.services

import com.bestDate.data.model.AppSettingsResponse
import com.bestDate.data.model.SubscriptionInfoRequest
import com.bestDate.data.model.SubscriptionInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface SubscriptionService {
    /**Get invitation list*/
    @Headers("isAuthorize: false")
    @GET("/api/v1/app/settings")
    suspend fun getAppSettings(): Response<AppSettingsResponse>

    /**Get user subscription*/
    @POST("/api/v1/user/subscription")
    suspend fun getUserSubscriptionInfo(@Body request: SubscriptionInfoRequest):
            Response<SubscriptionInfoResponse>

    /**Update subscription info*/
    @POST("/api/v1/subscriptions")
    suspend fun updateSubscriptionInfo(@Body request: SubscriptionInfoRequest):
            Response<SubscriptionInfoResponse>
}