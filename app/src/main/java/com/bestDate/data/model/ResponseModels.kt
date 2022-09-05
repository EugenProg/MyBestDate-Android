package com.bestDate.data.model

import android.os.Parcelable

abstract class BaseResponse: Parcelable {
    var success: Boolean = false
    var message: String = ""
}

data class AuthResponse(
    var token_type: String? = null,
    var expires_in: Int? = null,
    var access_token: String? = null,
    var refresh_token: String? = null
)