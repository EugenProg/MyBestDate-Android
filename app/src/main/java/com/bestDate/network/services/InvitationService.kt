package com.bestDate.network.services

import com.bestDate.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface InvitationService {
    /**Get invitation list*/
    @GET("/api/v1/invitations")
    suspend fun getInvitations(): Response<InvitationsListResponse>

    /**Send invitation*/
    @POST("/api/v1/invitations")
    suspend fun sendInvitation(@Body body: SendInvitationRequest): Response<SendInvitationResponse>

    /**Answer to invitation*/
    @PUT("/api/v1/invitations/{id}")
    suspend fun answerToInvitation(@Path("id") id: Int,
                                   @Body body: InvitationAnswerRequest): Response<BaseResponse>
}