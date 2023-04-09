package com.bestDate.network.remote

import com.bestDate.data.model.IdListRequest
import com.bestDate.network.services.GuestsService
import javax.inject.Inject

class GuestsRemoteData @Inject constructor(
    private val service: GuestsService
) {
    suspend fun getGuestsList(page: Int) = service.getGuestsList(page)
    suspend fun markGuestsViewed(body: IdListRequest) = service.markGuestsViewed(body)
}