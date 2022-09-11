package com.bestDate.network.services

import com.bestDate.data.model.ShortUserDataResponse
import com.bestDate.data.model.UserDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserService {
    /**Get user data*/
    @GET("/api/v1/user")
    suspend fun getUserData(@Header("Authorization") token: String): Response<UserDataResponse>

    /**Get user by id*/
    @GET("/api/v1/user/{id}")
    suspend fun getUserById(@Path("id") userId: Int,
                            @Header("Authorization") token: String): Response<ShortUserDataResponse>


}