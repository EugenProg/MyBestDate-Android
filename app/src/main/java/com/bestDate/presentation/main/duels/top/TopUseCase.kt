package com.bestDate.presentation.main.duels.top

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.DuelProfile
import com.bestDate.data.model.InternalException
import com.bestDate.network.remote.TopRemoteData
import com.bestDate.presentation.registration.Gender
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopUseCase @Inject constructor(
    private val topRemoteData: TopRemoteData
) {
    var topsResults: MutableLiveData<MutableList<DuelProfile>?> = MutableLiveData()
    var genderLocal = Gender.WOMAN

    suspend fun getTop(gender: Gender, country: String?) {
        val response = topRemoteData.getTop(gender.serverName, country)
        if (response.isSuccessful) {
            response.body()?.let {
                topsResults.postValue(it.data)
                genderLocal = gender
            }
        } else
            throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }
}