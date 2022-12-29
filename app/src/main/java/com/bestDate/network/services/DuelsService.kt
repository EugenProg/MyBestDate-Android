package com.bestDate.network.services

import com.bestDate.data.model.DuelProfileImageListResponse
import com.bestDate.data.model.DuelProfileResponse
import com.bestDate.data.model.DuelRequest
import com.bestDate.data.model.DuelVoteRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DuelsService {
    /**Get duels data*/
    @POST("/api/v1/voting-photos")
    suspend fun getDuels(@Body body: DuelRequest): Response<DuelProfileImageListResponse>

    /**Post duel vote*/
    @POST("/api/v1/voting")
    suspend fun postVote(@Body body: DuelVoteRequest): Response<DuelProfileResponse>
}