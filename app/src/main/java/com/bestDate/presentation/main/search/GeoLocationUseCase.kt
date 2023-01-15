package com.bestDate.presentation.main.search

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.GeocodingResponse
import com.bestDate.data.model.InternalException
import com.bestDate.data.utils.CityListItem
import com.bestDate.network.remote.GeocodingRemoteData
import javax.inject.Inject

class GeoLocationUseCase @Inject constructor(
    private val geocodingRemoteData: GeocodingRemoteData
) {
    var locationLiveData: MutableLiveData<GeocodingResponse?> = MutableLiveData()

    suspend fun getLocationByAddress(location: CityListItem?) {
        val response = geocodingRemoteData.getLocationByAddress(location)
        if (response.isSuccessful) {
            locationLiveData.postValue(response.body()?.firstOrNull())
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }
}