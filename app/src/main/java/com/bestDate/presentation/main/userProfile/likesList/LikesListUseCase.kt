package com.bestDate.presentation.main.userProfile.likesList

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.Like
import com.bestDate.data.model.LikesBody
import com.bestDate.data.model.ProfileImage
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LikesListUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData
) {

    var likesList: MutableLiveData<MutableList<Like>> = MutableLiveData()
    var photoMainResult: MutableLiveData<ProfileImage?> = MutableLiveData()

    suspend fun getLikes() {
        val response = userRemoteData.getUserLikes()
        if (response.isSuccessful) {
            response.body()?.let {
                likesList.postValue(it.data)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun like(body: LikesBody) {
        val response = userRemoteData.like(body)
        if (response.isSuccessful) {
            photoMainResult.postValue(response.body()?.data)
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    fun clearData() {
        likesList.postValue(mutableListOf())
    }
}