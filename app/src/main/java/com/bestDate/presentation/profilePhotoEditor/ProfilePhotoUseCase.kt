package com.bestDate.presentation.profilePhotoEditor

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.ProfileImage
import com.bestDate.network.remote.ImageRemoteData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfilePhotoUseCase @Inject constructor(
    private val imageRemoteData: ImageRemoteData
) {

    var savedImage: ProfileImage? = null

    suspend fun saveUserPhoto(image: ByteArray) {
        val requestFile =
            image.toRequestBody("multipart/form-data".toMediaTypeOrNull(), 0, image.size)
        val body = MultipartBody.Part.createFormData("photo", "name", requestFile)
        val response = imageRemoteData.saveUserPhoto(body)
        if (response.isSuccessful) {
            savedImage = response.body()?.data
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun deletePhoto(id: Int) {
        val response = imageRemoteData.deleteUserPhoto(id)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun updatePhotoStatus(id: Int, main: Boolean, top: Boolean) {
        val response = imageRemoteData.updatePhotoStatus(id, main, top)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }
}