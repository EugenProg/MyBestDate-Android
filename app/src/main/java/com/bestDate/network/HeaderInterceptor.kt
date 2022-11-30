package com.bestDate.network

import android.content.Context
import com.bestDate.R
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    val context: Context,
    private val preferencesUtils: PreferencesUtils
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = chain.request().newBuilder()
        val shouldAddAuthHeaders = original.headers["isAuthorize"] != "false"

        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("Accept", "application/json")
        builder.addHeader("X-Localization", context.getString(R.string.app_language))
        if (preferencesUtils.getString(Preferences.ACCESS_TOKEN).isNotEmpty() && shouldAddAuthHeaders
        ) {
            builder.addHeader("Authorization", preferencesUtils.getString(Preferences.ACCESS_TOKEN))
        }
        return chain.proceed(builder.build())
    }
}