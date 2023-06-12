package com.bestDate.network

import com.bestDate.data.model.InternalException
import com.bestDate.data.utils.Logger
import okhttp3.Interceptor
import okhttp3.Response

class ValidateInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestString = "${chain.request().method} ${chain.request().url.encodedPath} ${chain.request().body}"
        if (NetworkModule.requestIsValid(requestString)) {
            NetworkModule.addRequest(requestString)
            val response = chain.proceed(chain.request())
            NetworkModule.removeRequest(requestString)
            return response
        } else {
            Logger.print("Duplicate: $requestString")
            throw InternalException.RequestDuplicateException(requestString)
        }
    }
}