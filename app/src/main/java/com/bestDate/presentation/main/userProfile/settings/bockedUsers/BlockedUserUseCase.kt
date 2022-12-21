package com.bestDate.presentation.main.userProfile.settings.bockedUsers

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.ShortUserData
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockedUserUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData
) {

    var blockedUsers: MutableLiveData<MutableList<ShortUserData>> = MutableLiveData()

    suspend fun refreshBlockedUsersList() {
        val response = userRemoteData.getBlockedUsers()
        if (response.isSuccessful) {
            response.body()?.data?.let {
                blockedUsers.postValue(it)
            }
        } else throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun unlockUser(id: Int?) {
        val response = userRemoteData.unlockUser(id.orZero)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    fun clearData() {
        blockedUsers.postValue(mutableListOf())
    }
}