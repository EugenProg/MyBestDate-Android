package com.bestDate.network

import android.content.Context
import com.bestDate.data.extension.*
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
    @Core_url
    fun providesCoreBaseURL(): String = "https://dev-api.bestdate.info"

    @Provides
    @Geocoding_url
    fun providesGeocodingBaseURL(): String = "https://nominatim.openstreetmap.org"

    @Provides
    @Translate_url
    fun providesTranslateBaseURL(): String = "https://api-free.deepl.com/"

    @Provides
    fun provideInterceptor(
        @ApplicationContext context: Context, preferences: PreferencesUtils): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(context, preferences))
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    @Core_network
    fun provideCoreApi(@Core_url BASE_URL: String, interceptor: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(interceptor)
            .build()

    @Provides
    @Singleton
    @Geocoding_network
    fun provideGeocodingApi(@Geocoding_url BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Translate_network
    fun provideTranslateApi(@Translate_url BASE_URL: String, interceptor: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(interceptor)
            .build()

    @Provides
    @Singleton
    fun coreAuthApiService(@Core_network retrofit: Retrofit): CoreAuthService =
        retrofit.create(CoreAuthService::class.java)

    @Provides
    @Singleton
    fun duelsApiService(@Core_network retrofit: Retrofit): DuelsService =
        retrofit.create(DuelsService::class.java)

    @Provides
    @Singleton
    fun imageApiService(@Core_network retrofit: Retrofit): ImageApiService =
        retrofit.create(ImageApiService::class.java)

    @Provides
    @Singleton
    fun userApiService(@Core_network retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun questsApiService(@Core_network retrofit: Retrofit): GuestsService =
        retrofit.create(GuestsService::class.java)

    @Provides
    @Singleton
    fun invitationApiService(@Core_network retrofit: Retrofit): InvitationService =
        retrofit.create(InvitationService::class.java)

    @Provides
    @Singleton
    fun chatsApiService(@Core_network retrofit: Retrofit): ChatsService =
        retrofit.create(ChatsService::class.java)

    @Provides
    @Singleton
    fun geocodingApiService(@Geocoding_network retrofit: Retrofit): GeocodingService =
        retrofit.create(GeocodingService::class.java)

    @Provides
    @Singleton
    fun translationApiService(@Translate_network retrofit: Retrofit): TranslateService =
        retrofit.create(TranslateService::class.java)

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

    @Provides
    @Singleton
    fun duelsRemoteData(apiService: DuelsService): DuelsRemoteData =
        DuelsRemoteData(apiService)

    @Provides
    @Singleton
    fun chatsRemoteData(apiService: ChatsService): ChatsRemoteData =
        ChatsRemoteData(apiService)

    @Provides
    @Singleton
    fun geocodingRemoteData(apiService: GeocodingService): GeocodingRemoteData =
        GeocodingRemoteData(apiService)

    @Provides
    @Singleton
    fun translationRemoteData(apiService: TranslateService): TranslationRemoteData =
        TranslationRemoteData(apiService)
}