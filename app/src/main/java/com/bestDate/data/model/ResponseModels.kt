package com.bestDate.data.model

import android.os.Parcelable
import com.bestDate.data.extension.getTime
import com.bestDate.data.extension.getWeekdayWithTime
import com.bestDate.data.extension.isToday
import com.bestDate.db.entity.LocationDB
import com.bestDate.db.entity.UserDB
import kotlinx.parcelize.Parcelize

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

@Parcelize
data class ProfileImage(
    var id: Int? = null,
    var full_url: String? = null,
    var thumb_url: String? = null,
    var main: Boolean? = null,
    var top: Boolean? = null,
    var top_place: Int? = null,
    var liked: Boolean? = null,
    var likes: Int? = null
): Parcelable {
    fun copy(): ProfileImage {
        return ProfileImage(id, full_url, thumb_url, main, top, top_place, liked, likes)
    }
}

data class UserDataResponse(
    val data: UserDB
): BaseResponse()

data class LikesListResponse(
    val data: MutableList<Like>
): BaseResponse()

data class MatchesListResponse(
    val data: MutableList<Match>
): BaseResponse()

data class ShortUserDataResponse(
    val data: ShortUserData
): BaseResponse()

@Parcelize
data class ShortUserData(
    var id: Int? = null,
    var name: String? = null,
    var gender: String? = null,
    var language: String? = null,
    var birthday: String? = null,
    var full_questionnaire: Boolean? = null,
    var role: String? = null,
    var blocked: Boolean? = null,
    var blocked_me: Boolean? = null,
    var allow_chat: Boolean? = null,
    var is_online: Boolean? = null,
    var last_online_at: String? = null,
    var distance: Double? = null,
    var main_photo: ProfileImage? = null,
    var location: LocationDB? = null
): Parcelable {
    fun getLocation(): String {
        return "${location?.country.orEmpty()}, ${location?.city.orEmpty()}"
    }
}

data class Like(
    val id: Int? = null,
    val created_at: String? = null,
    val photo: ProfileImage? = null,
    val user: ShortUserData? = null
) {
    fun getLikeTime(): String {
        return if (created_at.isToday()) created_at.getTime() else created_at.getWeekdayWithTime()
    }
}

data class Match(
    val id: Int? = null,
    val viewed: Boolean? = null,
    val matched: Boolean? = null,
    val user: ShortUserData? = null,
    val created_at: String? = null
) {
    fun getTime(): String {
        return if (created_at.isToday()) created_at.getTime() else created_at.getWeekdayWithTime()
    }
}