package com.bestDate.presentation.main.userProfile.matchesList

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.Match
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchesListUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData
) {

    val matchesList: MutableLiveData<MutableList<Match>> = MutableLiveData()

    suspend fun getMatches() {
        val response = userRemoteData.getUserMatches()
        if (response.isSuccessful) {
            response.body()?.let {
                matchesList.postValue(it.data)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    fun clearData() {
        matchesList.postValue(mutableListOf())
    }
}