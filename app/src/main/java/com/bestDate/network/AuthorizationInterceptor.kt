package com.bestDate.network

import com.bestDate.data.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class AuthorizationInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!sessionManager.isAccessTokenExpired() || sessionManager.refreshToken.isNotEmpty()) {
            // Token is fresh or refresh token exists
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeaders(getToken())
            val originalRequest = chain.request()
            val response = chain.proceed(requestBuilder.build())
            // If token is expired or anyhow received unauthorized then try to refresh
            if (response.code == HttpsURLConnection.HTTP_UNAUTHORIZED || response.code == HttpsURLConnection.HTTP_FORBIDDEN) {
                response.close()
                synchronized(this) {
                    if (updateTokens()) {
                        // Token refreshed, try again
                        val newCall = chain.request().newBuilder().addHeaders(getToken()).build()
                        chain.proceedDeletingTokenOnError(newCall)
                    } else {
                        // Token was expired and can't be refreshed, return
                        chain.proceedDeletingTokenOnError(originalRequest)
                    }
                }
            } else {
                // Response was successful
                response
            }
        } else {
            // Token has expired and there is no refresh token
            chain.proceedDeletingTokenOnError(chain.request())
        }
    }

    private fun Request.Builder.addHeaders(token: String) =
        this.apply { header("Authorization", token) }

    private fun getToken(): String {
        return sessionManager.accessToken
    }

    private fun updateTokens(): Boolean {
        return sessionManager.refreshToken.isNotEmpty() && (sessionManager.refreshToken().isSuccessful)
    }

    private fun Interceptor.Chain.proceedDeletingTokenOnError(request: Request): Response {
        val response = proceed(request)
        if (response.code == HttpsURLConnection.HTTP_UNAUTHORIZED || response.code == HttpsURLConnection.HTTP_FORBIDDEN) {
            sessionManager.clearSession()
        }
        return response
    }
}

