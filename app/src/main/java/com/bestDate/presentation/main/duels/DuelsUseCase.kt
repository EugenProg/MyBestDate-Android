package com.bestDate.presentation.main.duels

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.*
import com.bestDate.network.remote.DuelsRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DuelsUseCase @Inject constructor(
    private val duelsRemoteData: DuelsRemoteData
) {
    var duelProfiles: MutableList<ProfileImage>? = null
    var duelResults: MutableList<DuelProfile>? = null

    suspend fun getMyDuels(gender: String, country: String?) {
        val response = duelsRemoteData.getDuels(gender, country)
        if (response.isSuccessful) {
            response.body()?.let {
                duelProfiles = it.data
            }
        } else {
            if (response.code() == 404)
                duelProfiles = mutableListOf()
            else
                throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
        }
    }

    suspend fun postVote(winningPhoto: Int, loserPhoto: Int) {
        val response = duelsRemoteData.postVote(winningPhoto, loserPhoto)
        if (response.isSuccessful) {
            response.body()?.let {
                duelResults = it.data
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    fun clearData() {
        duelProfiles = mutableListOf()
        duelResults = mutableListOf()
    }
}