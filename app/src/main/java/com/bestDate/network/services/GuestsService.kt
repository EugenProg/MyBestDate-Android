package com.bestDate.network.services

import com.bestDate.data.model.GuestsResponse
import retrofit2.Response
import retrofit2.http.GET

interface GuestsService {
    @GET("/api/v1/guests")
    suspend fun getGuestsList(): Response<GuestsResponse>
}