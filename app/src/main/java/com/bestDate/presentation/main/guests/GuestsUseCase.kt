package com.bestDate.presentation.main.guests

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.model.Guest
import com.bestDate.data.model.InternalException
import com.bestDate.network.remote.GuestsRemoteData
import javax.inject.Inject

class GuestsUseCase @Inject constructor(
    private val guestsRemoteData: GuestsRemoteData
) {
    var guestsListNew: MutableLiveData<MutableList<Guest>> = MutableLiveData()
    var guestsListPrev: MutableLiveData<MutableList<Guest>> = MutableLiveData()
    var guestsList: MutableLiveData<MutableList<Guest>> = MutableLiveData()

    suspend fun getGuestsList() {
        val response = guestsRemoteData.getGuestsList()
        if (response.isSuccessful) {
            response.body()?.let {
                guestsList.postValue(it.data)
                guestsListNew.postValue(it.data.filter { it.viewed }.toMutableList())
                guestsListPrev.postValue(it.data.filter { !it.viewed }.toMutableList())
            }
        } else throw InternalException.OperationException(response.message())
    }

    fun clearData() {
        guestsListNew.postValue(mutableListOf())
        guestsListPrev.postValue(mutableListOf())
        guestsList.postValue(mutableListOf())
    }
}