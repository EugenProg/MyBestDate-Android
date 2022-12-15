package com.bestDate.network.services

import com.bestDate.data.model.BaseResponse
import com.bestDate.data.model.GuestsResponse
import com.bestDate.data.model.IdListRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface GuestsService {
    @GET("/api/v1/guests")
    suspend fun getGuestsList(): Response<GuestsResponse>

    @PUT("/api/v1/guests")
    suspend fun markGuestsViewed(@Body body: IdListRequest): Response<BaseResponse>
}