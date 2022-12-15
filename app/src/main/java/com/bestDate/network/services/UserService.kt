package com.bestDate.network.services

import com.bestDate.data.model.BaseResponse
import com.bestDate.data.model.LikesListResponse
import com.bestDate.data.model.UserDataResponse
import com.bestDate.data.model.*
import com.bestDate.db.entity.QuestionnaireDB
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    /**Get user data*/
    @GET("/api/v1/user")
    suspend fun getUserData(): Response<UserDataResponse>

    /**Get user by id*/
    @GET("/api/v1/users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<UserDataResponse>

    /**Save questionnaire*/
    @PUT("/api/v1/user/questionnaire")
    suspend fun saveQuestionnaire(@Body questionnaire: QuestionnaireDB): Response<BaseResponse>

    /**Get user likes list*/
    @GET("/api/v1/likes")
    suspend fun getLikesList(): Response<LikesListResponse>

    /**Get user matches list*/
    @GET("/api/v1/match")
    suspend fun getMatchesList(): Response<MatchesListResponse>

    /**Get user duels*/
    @GET("/api/v1/voting")
    suspend fun getMyDuels(): Response<MyDuelsResponse>

    /**Block user*/
    @POST("/api/v1/block-user/{id}")
    suspend fun blockUser(@Path("id") userId: Int): Response<BaseResponse>

    /**Unlock user*/
    @POST("/api/v1/unlock-user/{id}")
    suspend fun unlockUser(@Path("id") userId: Int): Response<BaseResponse>

    /**Change language*/
    @PUT("/api/v1/settings/language")
    suspend fun changeLanguage(@Body body: RequestLanguage): Response<UserDataResponse>

    /**Get users*/
    @POST("/api/v1/users")
    suspend fun getUsers(
        @Body filters: FilterOptions,
        @Query("page") page: Int
    ): Response<ShortUserListDataResponse>

    /**Get user invitations*/
    @POST("/api/v1/user/invitations")
    suspend fun getInvitations(@Body body: UserInvitationRequest): Response<UserInvitationsResponse>
}