package com.bestDate.network.remote

import com.bestDate.data.utils.CityListItem
import com.bestDate.network.services.GeocodingService
import javax.inject.Inject

class GeocodingRemoteData @Inject constructor(
    private val service: GeocodingService
) {
    suspend fun getLocationByAddress(location: CityListItem?) =
        service.getLocationByAddress("json", 1, location?.city.orEmpty(), location?.country.orEmpty())
}