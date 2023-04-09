package com.bestDate.presentation.main.userProfile.likesList

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.LikesBody
import com.bestDate.data.model.ProfileImage
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LikesListUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData
) {

    var likesList = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false, initialLoadSize = 1),
        pagingSourceFactory = { LikesPagingSource(userRemoteData) }
    ).flow

    var photoMainResult: MutableLiveData<ProfileImage?> = MutableLiveData()

    suspend fun like(body: LikesBody) {
        val response = userRemoteData.like(body)
        if (response.isSuccessful) {
            photoMainResult.postValue(response.body()?.data)
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }
}