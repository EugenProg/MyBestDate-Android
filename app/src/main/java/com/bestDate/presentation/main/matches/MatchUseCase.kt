package com.bestDate.presentation.main.matches

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.Match
import com.bestDate.data.model.ShortUserData
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchUseCase @Inject constructor(
    private val remoteData: UserRemoteData
) {

    val matchesList: MutableLiveData<MutableList<ShortUserData>> = MutableLiveData(mutableListOf())
    val matchAction: MutableLiveData<Match?> = MutableLiveData()

    suspend fun getUsersForMatch() {
        val response = remoteData.getUsersForMatch()
        if (response.isSuccessful) {
            response.body()?.data?.let {
                matchesList.postValue(it)
            }
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun matchAction(userId: Int) {
        val response = remoteData.matchAction(userId)
        if (response.isSuccessful) {
            response.body()?.data?.let {
                if (it.matched == true) matchAction.postValue(it)
            }
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    fun nextUser() {
        matchesList.value?.removeLast()
    }

    fun clearMatch() {
        matchAction.value = null
    }
}