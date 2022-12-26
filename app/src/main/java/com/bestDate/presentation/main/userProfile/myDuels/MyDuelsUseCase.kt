package com.bestDate.presentation.main.userProfile.myDuels

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.MyDuel
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyDuelsUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData
) {

    var myDuels: MutableLiveData<MutableList<MyDuel>> = MutableLiveData()

    suspend fun getMyDuels() {
        val response = userRemoteData.getMyDuels()
        if (response.isSuccessful) {
            response.body()?.let {
                myDuels.postValue(it.data)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    fun clearData() {
        myDuels.postValue(mutableListOf())
    }
}