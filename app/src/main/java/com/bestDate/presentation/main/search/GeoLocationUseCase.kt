package com.bestDate.presentation.main.search

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.GeocodingResponse
import com.bestDate.data.model.InternalException
import com.bestDate.data.utils.CityListItem
import com.bestDate.network.remote.GeocodingRemoteData
import javax.inject.Inject

class GeoLocationUseCase @Inject constructor(
    private val geocodingRemoteData: GeocodingRemoteData
) {
    var location: GeocodingResponse? = null

    suspend fun getLocationByAddress(location: CityListItem?) {
        val response = geocodingRemoteData.getLocationByAddress(location)
        if (response.isSuccessful) {
            this.location = response.body()?.firstOrNull()
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }
}