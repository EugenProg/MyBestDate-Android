package com.bestDate.network.services

import com.bestDate.data.model.BaseResponse
import com.bestDate.data.model.GuestsResponse
import com.bestDate.data.model.IdListRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface GuestsService {
    @GET("/api/v1/guests")
    suspend fun getGuestsList(@Query("page") page: Int): Response<GuestsResponse>

    @PUT("/api/v1/guests")
    suspend fun markGuestsViewed(@Body body: IdListRequest): Response<BaseResponse>
}