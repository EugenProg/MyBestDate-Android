package com.bestDate.presentation.main.userProfile.likesList

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.Like
import com.bestDate.data.utils.Logger
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LikesListUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData
) {

    var likesList: MutableLiveData<MutableList<Like>> = MutableLiveData()

    suspend fun getLikes() {
        val response = userRemoteData.getUserLikes()
        if (response.isSuccessful) {
            response.body()?.let {
                likesList.postValue(it.data)
            }
        } else throw InternalException.OperationException(response.message())
    }
}