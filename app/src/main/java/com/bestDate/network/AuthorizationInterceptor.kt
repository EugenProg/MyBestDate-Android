package com.bestDate.network

import com.bestDate.data.model.InternalException
import com.bestDate.data.utils.NetworkStateListener
import com.bestDate.data.utils.NetworkStatus
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
        val shouldAddAuthHeaders = chain.request().headers["isAuthorize"] != "false"

        if (NetworkStateListener.currentStatus == NetworkStatus.LOST)
            throw InternalException.NotConnectionException()

        return if (shouldAddAuthHeaders) {
            if (sessionManager.isAccessTokenExpired() || sessionManager.refreshToken.isBlank()) {
                synchronized(this) {
                    makeRequestWithRefresh(chain)
                }
            } else {
                makeRequest(chain)
            }
        } else {
            chain.proceed(chain.request())
        }
    }

    private fun makeRequestWithRefresh(chain: Interceptor.Chain) =
        if (updateTokens()) makeRequest(chain)
        else chain.proceedDeletingTokenOnError(chain.request())

    private fun makeRequest(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addAuthorizationHeader().build()
        val response = chain.proceed(request)
        return if (response.code == HttpsURLConnection.HTTP_UNAUTHORIZED ||
            response.code == HttpsURLConnection.HTTP_FORBIDDEN
        ) {
            response.close()
            synchronized(this) {
                makeRequestWithRefresh(chain)
            }
        } else response
    }

    private fun Request.Builder.addAuthorizationHeader() =
        this.apply { header("Authorization", getToken()) }

    private fun getToken(): String {
        return sessionManager.accessToken
    }

    private fun updateTokens(): Boolean {
        return sessionManager.refreshToken.isNotEmpty() && (sessionManager.refreshToken().isSuccessful)
    }

    private fun Interceptor.Chain.proceedDeletingTokenOnError(request: Request): Response {
        val response = proceed(request)
        if (response.code == HttpsURLConnection.HTTP_UNAUTHORIZED ||
            response.code == HttpsURLConnection.HTTP_FORBIDDEN
        ) {
            sessionManager.clearSession()
        }
        return response
    }
}

