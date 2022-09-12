package com.bestDate.network.services

import com.bestDate.data.model.BaseResponse
import com.bestDate.data.model.ShortUserDataResponse
import com.bestDate.data.model.UserDataResponse
import com.bestDate.db.entity.QuestionnaireDB
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    /**Get user data*/
    @GET("/api/v1/user")
    suspend fun getUserData(@Header("Authorization") token: String): Response<UserDataResponse>

    /**Get user by id*/
    @GET("/api/v1/user/{id}")
    suspend fun getUserById(@Path("id") userId: Int,
                            @Header("Authorization") token: String): Response<ShortUserDataResponse>

    /**Save questionnaire*/
    @PUT("/api/v1/user/questionnaire")
    suspend fun saveQuestionnaire(@Body questionnaire: QuestionnaireDB,
                                  @Header("Authorization") token: String): Response<BaseResponse>
}