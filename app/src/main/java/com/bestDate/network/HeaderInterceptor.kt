package com.bestDate.network

import android.content.Context
import com.bestDate.R
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    val context: Context,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("Accept", "application/json")
        builder.addHeader("X-Localization", context.getString(R.string.app_locale))
        return chain.proceed(builder.build())
    }
}