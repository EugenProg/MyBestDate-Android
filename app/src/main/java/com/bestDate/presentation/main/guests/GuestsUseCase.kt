package com.bestDate.presentation.main.guests

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.Guest
import com.bestDate.data.model.IdListRequest
import com.bestDate.data.model.InternalException
import com.bestDate.network.remote.GuestsRemoteData
import javax.inject.Inject

class GuestsUseCase @Inject constructor(
    private val guestsRemoteData: GuestsRemoteData
) {
    var guestsListNew: MutableLiveData<MutableList<Guest>> = MutableLiveData()
    var guestsListPrev: MutableLiveData<MutableList<Guest>> = MutableLiveData()
    var guestsListIsEmpty: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun getGuestsList() {
        val response = guestsRemoteData.getGuestsList()
        if (response.isSuccessful) {
            response.body()?.let {
                guestsListNew.postValue(it.data?.filter { it.viewed == false }?.toMutableList())
                guestsListPrev.postValue(it.data?.filter { it.viewed == true }?.toMutableList())
                guestsListIsEmpty.postValue(it.data?.isEmpty())
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun markGuestsViewed(body: IdListRequest) {
        val response = guestsRemoteData.markGuestsViewed(body)
        if (response.isSuccessful) {
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    fun clearData() {
        guestsListNew.postValue(mutableListOf())
        guestsListPrev.postValue(mutableListOf())
        guestsListIsEmpty.postValue(false)
    }
}