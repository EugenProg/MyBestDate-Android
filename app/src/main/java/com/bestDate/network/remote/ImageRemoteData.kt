package com.bestDate.network.remote

import com.bestDate.data.model.BaseResponse
import com.bestDate.data.model.PhotoStatusUpdateRequest
import com.bestDate.network.services.ImageApiService
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class ImageRemoteData @Inject constructor(
    private var service: ImageApiService
) {

    suspend fun saveUserPhoto(moderated: Boolean, image: MultipartBody.Part) =
        service.saveProfileImage(moderated, image)

    suspend fun deleteUserPhoto(id: Int): Response<BaseResponse> =
        service.deleteUserPhoto(id)

    suspend fun updatePhotoStatus(id: Int, main: Boolean, top: Boolean) =
        service.updateUserPhoto(id, PhotoStatusUpdateRequest(main, top))
}