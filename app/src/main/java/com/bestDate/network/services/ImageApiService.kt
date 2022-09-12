package com.bestDate.network.services

import com.bestDate.data.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ImageApiService {
    /**Load user photo*/
    @Multipart
    @POST("/api/v1/user/photos")
    suspend fun saveProfileImage(@Part image: MultipartBody.Part,
                                 @Header("Authorization") auth: String): Response<ProfileImageResponse>

    /**Delete profile image*/
    @DELETE("/api/v1/user/photos")
    suspend fun deleteUserPhoto(@Body params: IdListRequest,
                                @Header("Authorization") auth: String): Response<BaseResponse>

    /**Update image status*/
    @PUT("/api/v1/user/photos/{id}")
    suspend fun updateUserPhoto(@Path("id") id: Int,
                                @Body params: PhotoStatusUpdateRequest,
                                @Header("Authorization") auth: String): Response<BaseResponse>

}