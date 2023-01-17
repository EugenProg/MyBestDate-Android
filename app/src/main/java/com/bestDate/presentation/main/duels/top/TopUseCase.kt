package com.bestDate.presentation.main.duels.top

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.network.remote.TopRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopUseCase @Inject constructor(
    private val topRemoteData: TopRemoteData
) {
    suspend fun getTop(gender: String, country: String?) {
        val response = topRemoteData.getTop(gender, country)
        if (response.isSuccessful) {
//            response.body()?.let {
//                duelProfiles = it.data
//            }
        } else
            throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

}