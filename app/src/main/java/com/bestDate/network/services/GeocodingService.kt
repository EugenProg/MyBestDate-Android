package com.bestDate.network.services

import com.bestDate.data.model.GeocodingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingService {
    /**Get location by address*/
    @GET("/search")
    suspend fun getLocationByAddress(
        @Query("format") format: String,
        @Query("addressdetails") details: Int,
        @Query("city") city: String,
        @Query("country") country: String
    ): Response<MutableList<GeocodingResponse>>
}