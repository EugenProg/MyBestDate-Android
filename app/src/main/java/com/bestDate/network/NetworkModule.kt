package com.bestDate.network

import android.content.Context
import com.bestDate.network.remote.AuthRemoteData
import com.bestDate.network.services.CoreAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun providesBaseURL(): String = "https://dev-api.bestdate.info"

    @Provides
    fun provideInterceptor(@ApplicationContext context: Context): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(context))
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideCoreApi(BASE_URL: String, interceptor: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(interceptor)
            .build()

    @Provides
    @Singleton
    fun coreAuthApiService(retrofit: Retrofit): CoreAuthService =
        retrofit.create(CoreAuthService::class.java)

    @Provides
    @Singleton
    fun authRemoteData(apiService: CoreAuthService): AuthRemoteData = AuthRemoteData(apiService)
}