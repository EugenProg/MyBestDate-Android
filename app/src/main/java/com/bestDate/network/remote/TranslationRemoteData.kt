package com.bestDate.network.remote

import com.bestDate.network.services.TranslateService
import javax.inject.Inject

class TranslationRemoteData @Inject constructor(
    private val service: TranslateService
) {
    suspend fun translate(text: String, lang: String) =
        service.translate(
        "9b770114-c5bb-cc0d-de5c-c7c2cdbad9c6:fx", text, lang
        )
}