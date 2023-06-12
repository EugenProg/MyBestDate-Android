package com.bestDate.network.services

import com.bestDate.data.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ImageApiService {
    /**Load user photo*/
    @Multipart
    @POST("/api/v1/user/photos")
    suspend fun saveProfileImage(
        @Query("moderated") moderated: Int,
        @Part image: MultipartBody.Part
    ): Response<ProfileImageResponse>

    /**Delete profile image*/
    @DELETE("/api/v1/user/photos/{id}")
    suspend fun deleteUserPhoto(@Path("id") id: Int): Response<BaseResponse>

    /**Update image status*/
    @PUT("/api/v1/user/photos/{id}")
    suspend fun updateUserPhoto(
        @Path("id") id: Int,
        @Body params: PhotoStatusUpdateRequest
    ): Response<BaseResponse>

}