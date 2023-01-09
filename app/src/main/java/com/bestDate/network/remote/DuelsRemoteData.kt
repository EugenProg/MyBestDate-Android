package com.bestDate.network.remote

import com.bestDate.data.model.DuelRequest
import com.bestDate.data.model.DuelVoteRequest
import com.bestDate.network.services.DuelsService
import javax.inject.Inject

class DuelsRemoteData @Inject constructor(
    private val duelsService: DuelsService
) {
    suspend fun getDuels(gender: String, country: String?) =
        duelsService.getDuels(DuelRequest(gender, country))

    suspend fun postVote(winningPhoto: Int, loserPhoto: Int) =
        duelsService.postVote(DuelVoteRequest(winningPhoto, loserPhoto))
}