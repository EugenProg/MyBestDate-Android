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
    var topsWoman: MutableLiveData<MutableList<DuelProfile>> = MutableLiveData()
    var topsMan: MutableLiveData<MutableList<DuelProfile>> = MutableLiveData()

    suspend fun getTop(gender: Gender) {
        if (gender == Gender.WOMAN) {
            getWomanTop()
            getManTop()
        } else {
            getManTop()
            getWomanTop()
        }
    }

    private suspend fun getWomanTop() {
        val response = topRemoteData.getTop(Gender.WOMAN.serverName)
        if (response.isSuccessful) {
            response.body()?.data?.let {
                topsWoman.postValue(it)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    private suspend fun getManTop() {
        val response = topRemoteData.getTop(Gender.MAN.serverName)
        if (response.isSuccessful) {
            response.body()?.data?.let {
                topsMan.postValue(it)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    fun clearData() {
        topsWoman.postValue(mutableListOf())
        topsMan.postValue(mutableListOf())
    }
}