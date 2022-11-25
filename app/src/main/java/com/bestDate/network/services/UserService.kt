package com.bestDate.network.services

import com.bestDate.data.model.*
import com.bestDate.db.entity.QuestionnaireDB
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    /**Get user data*/
    @GET("/api/v1/user")
    suspend fun getUserData(@Header("Authorization") token: String): Response<UserDataResponse>

    /**Get user by id*/
    @GET("/api/v1/user/{id}")
    suspend fun getUserById(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Response<ShortUserDataResponse>

    /**Save questionnaire*/
    @PUT("/api/v1/user/questionnaire")
    suspend fun saveQuestionnaire(
        @Body questionnaire: QuestionnaireDB,
        @Header("Authorization") token: String
    ): Response<BaseResponse>

    /**Get users*/
    @POST("/api/v1/users")
    suspend fun getUsers(
        @Header("Authorization") token: String,
        @Body filters: FilterOptions,
        @Query("page") page: Int = 2
    ): Response<ShortUserListDataResponse>
}