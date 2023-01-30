package com.bestDate.presentation.main.duels

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.*
import com.bestDate.network.remote.DuelsRemoteData
import com.bestDate.presentation.registration.Gender
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DuelsUseCase @Inject constructor(
    private val duelsRemoteData: DuelsRemoteData
) {
    var duelProfiles: MutableLiveData<MutableList<ProfileImage>?> = MutableLiveData()
    var duelResults: MutableLiveData<MutableList<DuelProfile>?> = MutableLiveData()
    var genderLocal: MutableLiveData<Gender> = MutableLiveData()

    suspend fun getMyDuels(gender: Gender, country: String?) {
        val response = duelsRemoteData.getDuels(gender.serverName, country)
        if (response.isSuccessful) {
            response.body()?.let {
                duelProfiles.postValue(it.data)
                genderLocal.postValue(gender)
            }
        } else {
            if (response.code() == 404) {
                duelProfiles.postValue(mutableListOf())
                genderLocal.postValue(gender)
            }
            else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
        }
    }

    suspend fun postVote(winningPhoto: Int, loserPhoto: Int) {
        val response = duelsRemoteData.postVote(winningPhoto, loserPhoto)
        if (response.isSuccessful) {
            response.body()?.let {
                duelResults.postValue(it.data)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    fun clearData() {
        duelProfiles.postValue(mutableListOf())
        duelResults.postValue(mutableListOf())
    }
}