package com.bestDate.network.services

import com.bestDate.data.model.BaseResponse
import com.bestDate.data.model.LikesListResponse
import com.bestDate.data.model.ShortUserDataResponse
import com.bestDate.data.model.UserDataResponse
import com.bestDate.data.model.*
import com.bestDate.data.model.*
import com.bestDate.db.entity.QuestionnaireDB
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    /**Get user data*/
    @GET("/api/v1/user")
    suspend fun getUserData(): Response<UserDataResponse>

    /**Get user by id*/
    @GET("/api/v1/user/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<ShortUserDataResponse>

    /**Save questionnaire*/
    @PUT("/api/v1/user/questionnaire")
    suspend fun saveQuestionnaire(@Body questionnaire: QuestionnaireDB): Response<BaseResponse>

    /**Get user likes list*/
    @GET("/api/v1/likes")
    suspend fun getLikesList(): Response<LikesListResponse>

    /**Get user matches list*/
    @GET("/api/v1/match")
    suspend fun getMatchesList(): Response<MatchesListResponse>

    /**Get users*/
    @POST("/api/v1/users")
    suspend fun getUsers(
        @Body filters: FilterOptions,
        @Query("page") page: Int = 2
    ): Response<ShortUserListDataResponse>
}