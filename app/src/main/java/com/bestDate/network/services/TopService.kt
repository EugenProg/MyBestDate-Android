package com.bestDate.network.services

import com.bestDate.data.model.DuelProfileResponse
import com.bestDate.data.model.DuelRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface TopService {
    /**Get top data*/
    @POST("/api/v1/top")
    suspend fun getTop(@Body body: DuelRequest, @Query("page") page: Int): Response<DuelProfileResponse>
}