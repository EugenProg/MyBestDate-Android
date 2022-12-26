package com.bestDate.presentation.main.userProfile.personalData.setLocation

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.GeocodingResponse
import com.bestDate.data.model.InternalException
import com.bestDate.data.utils.CityListItem
import com.bestDate.db.dao.UserDao
import com.bestDate.network.remote.GeocodingRemoteData
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetUserLocationUseCase @Inject constructor(
    private val userDao: UserDao,
    private val userRemoteData: UserRemoteData,
    private val geocodingRemoteData: GeocodingRemoteData
) {

    suspend fun saveUserLocation(location: CityListItem) {
        val locationRequest = getLocationByAddress(location)
        val response =
            locationRequest?.let { userRemoteData.saveUserLocation(it.getSaveUserRequest()) }
        if (response?.isSuccessful == true) {
            response.body()?.let {
                userDao.validate(it.data)
            }
        } else throw InternalException.OperationException(response?.errorBody().getErrorMessage())
    }

    private suspend fun getLocationByAddress(location: CityListItem): GeocodingResponse? {
        val response = geocodingRemoteData.getLocationByAddress(location)
        if (response.isSuccessful) {
            return response.body()?.firstOrNull()
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }
}