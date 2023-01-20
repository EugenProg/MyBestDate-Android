package com.bestDate.presentation.main.guests

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.Guest
import com.bestDate.data.model.IdListRequest
import com.bestDate.data.model.InternalException
import com.bestDate.network.remote.GuestsRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuestsUseCase @Inject constructor(
    private val guestsRemoteData: GuestsRemoteData
) {
    var guestsListNew: MutableLiveData<MutableList<Guest>> = MutableLiveData()
    var guestsListPrev: MutableLiveData<MutableList<Guest>> = MutableLiveData()
    var guestsList: MutableLiveData<MutableList<Guest>?> = MutableLiveData()

    suspend fun getGuestsList() {
        val response = guestsRemoteData.getGuestsList()
        if (response.isSuccessful) {
            response.body()?.let {
                guestsList.postValue(it.data)
                guestsListNew.postValue(it.data?.filter { it.viewed == false }?.toMutableList())
                guestsListPrev.postValue(it.data?.filter { it.viewed == true }?.toMutableList())
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun markGuestsViewed(body: IdListRequest) {
        val response = guestsRemoteData.markGuestsViewed(body)
        if (!response.isSuccessful)
            throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    fun clearData() {
        guestsListNew.postValue(mutableListOf())
        guestsListPrev.postValue(mutableListOf())
        guestsList.postValue(null)
    }
}