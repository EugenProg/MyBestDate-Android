package com.bestDate.presentation.main.anotherProfile

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.ProfileImage
import com.bestDate.db.entity.UserDB
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnotherProfileUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData
) {

    var user: MutableLiveData<UserDB> = MutableLiveData()
    var photos:  MutableLiveData<MutableList<ProfileImage>?> = MutableLiveData()

    suspend fun getUserById(id: Int?) {
        val response = userRemoteData.getUserById(id.orZero)
        if (response.isSuccessful) {
            response.body()?.let {
                user.postValue(it.data)
                photos.postValue(it.data.photos)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun blockUser(id: Int?) {
        val response = userRemoteData.blockUser(id.orZero)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun unlockUser(id: Int?) {
        val response = userRemoteData.unlockUser(id.orZero)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }
}