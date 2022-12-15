package com.bestDate.network

import android.content.Context
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.network.remote.*
import com.bestDate.network.services.*
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
    fun provideInterceptor(
        @ApplicationContext context: Context,
        preferences: PreferencesUtils
    ): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(context, preferences))
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
    fun imageApiService(retrofit: Retrofit): ImageApiService =
        retrofit.create(ImageApiService::class.java)

    @Provides
    @Singleton
    fun userApiService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun questsApiService(retrofit: Retrofit): GuestsService =
        retrofit.create(GuestsService::class.java)

    @Provides
    @Singleton
    fun invitationApiService(retrofit: Retrofit): InvitationService =
        retrofit.create(InvitationService::class.java)

    @Provides
    @Singleton
    fun authRemoteData(apiService: CoreAuthService): AuthRemoteData =
        AuthRemoteData(apiService)

    @Provides
    @Singleton
    fun imageRemoteData(apiService: ImageApiService): ImageRemoteData =
        ImageRemoteData(apiService)

    @Provides
    @Singleton
    fun userRemoteData(apiService: UserService): UserRemoteData =
        UserRemoteData(apiService)

    @Provides
    @Singleton
    fun guestsRemoteData(apiService: GuestsService): GuestsRemoteData =
        GuestsRemoteData(apiService)

    @Provides
    @Singleton
    fun invitationRemoteData(apiService: InvitationService): InvitationsRemoteData =
        InvitationsRemoteData(apiService)
}