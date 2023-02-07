package com.bestDate.network.services

import com.bestDate.data.model.GetGoogleAccessTokenRequest
import com.bestDate.data.model.GoogleAccessTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GoogleAuthService {
    @Headers(value = ["Content-Type: application/json"])
    @POST("/oauth2/v4/token")
    suspend fun getAccessToken(@Body request: GetGoogleAccessTokenRequest):
            Response<GoogleAccessTokenResponse>
}