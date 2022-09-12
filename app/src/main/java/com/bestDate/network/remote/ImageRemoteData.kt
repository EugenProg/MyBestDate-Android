package com.bestDate.network.remote

import com.bestDate.data.model.BaseResponse
import com.bestDate.data.model.IdListRequest
import com.bestDate.data.model.PhotoStatusUpdateRequest
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.network.services.ImageApiService
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class ImageRemoteData @Inject constructor(
    private var service: ImageApiService,
    private val preferencesUtils: PreferencesUtils) {

    suspend fun saveUserPhoto(image: MultipartBody.Part) =
        service.saveProfileImage(image, preferencesUtils.getString(Preferences.ACCESS_TOKEN))

    suspend fun deleteUserPhoto(id: Int): Response<BaseResponse> =
         service.deleteUserPhoto(id, preferencesUtils.getString(Preferences.ACCESS_TOKEN))

    suspend fun updatePhotoStatus(id: Int, main: Boolean, top: Boolean) =
        service.updateUserPhoto(id, PhotoStatusUpdateRequest(main, top),
            preferencesUtils.getString(Preferences.ACCESS_TOKEN))
}