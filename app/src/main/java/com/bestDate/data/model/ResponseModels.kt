package com.bestDate.data.model

import com.bestDate.db.entity.LocationDB
import com.bestDate.db.entity.UserDB

open class BaseResponse {
    var success: Boolean = false
    var message: String = ""
}

data class AuthResponse(
    var token_type: String? = null,
    var expires_in: Int? = null,
    var access_token: String? = null,
    var refresh_token: String? = null
)

data class ProfileImageResponse(
    var data: ProfileImage? = null
): BaseResponse()

data class ProfileImage(
    var id: Int? = null,
    var full_url: String? = null,
    var thumb_url: String? = null,
    var main: Boolean? = null,
    var top: Boolean? = null,
    var liked: Boolean? = null
) {
    fun copy(): ProfileImage {
        return ProfileImage(id, full_url, thumb_url, main, top, liked)
    }
}

data class UserDataResponse(
    val data: UserDB
): BaseResponse()

data class ShortUserDataResponse(
    val data: ShortUserData
): BaseResponse()

data class ShortUserData(
    var id: Int? = null,
    var name: String? = null,
    var gender: String? = null,
    var birthday: String? = null,
    var main_photo: ProfileImage? = null,
    var is_online: Boolean? = null,
    var last_online_at: String? = null,
    var location: LocationDB? = null,
    var block_messages: Boolean? = null,
    var full_questionnaire: Boolean? = null,
    var distance: Double? = null,
)