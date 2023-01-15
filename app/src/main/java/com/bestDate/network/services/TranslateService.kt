package com.bestDate.network.services

import com.bestDate.data.model.TranslationResponse
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface TranslateService {
    /**Translate*/
    @Headers(value = ["Content-Type: application/x-www-form-urlencoded",
        "Accept: application/json"])
    @POST("v2/translate")
    suspend fun translate(@Query("auth_key") key: String,
                          @Query("text") text: String,
                          @Query("target_lang") lang: String): Response<TranslationResponse>
}