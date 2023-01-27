package com.bestDate.presentation.main.duels.top

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.DuelProfile
import com.bestDate.data.model.InternalException
import com.bestDate.network.remote.TopRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopUseCase @Inject constructor(
    private val topRemoteData: TopRemoteData
) {
    var topsResults: MutableLiveData<MutableList<DuelProfile>?> = MutableLiveData()

    suspend fun getTop(gender: String, country: String?) {
        val response = topRemoteData.getTop(gender, country)
        if (response.isSuccessful) {
            response.body()?.let {
                topsResults.postValue(it.data)
            }
        } else
            throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

}