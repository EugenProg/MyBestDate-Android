package com.bestDate.network.remote

import com.bestDate.data.model.DuelRequest
import com.bestDate.network.services.TopService
import javax.inject.Inject

class TopRemoteData @Inject constructor(
    private val topService: TopService
) {
    suspend fun getTop(gender: String, country: String? = null) =
        topService.getTop(DuelRequest(gender, country))
}