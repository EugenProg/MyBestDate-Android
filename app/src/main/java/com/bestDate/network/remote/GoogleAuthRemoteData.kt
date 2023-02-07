package com.bestDate.network.remote

import com.bestDate.data.model.GetGoogleAccessTokenRequest
import com.bestDate.network.services.GoogleAuthService
import javax.inject.Inject

class GoogleAuthRemoteData @Inject constructor(private val service: GoogleAuthService) {
    suspend fun getGoogleAuthToken(authCode: String?) =
        service.getAccessToken(GetGoogleAccessTokenRequest(code = authCode.orEmpty()))
}