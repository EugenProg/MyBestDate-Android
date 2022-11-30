package com.bestDate.data.model

import com.bestDate.data.extension.CalendarUtils
import androidx.room.Embedded
import androidx.room.Entity
import com.bestDate.db.entity.LocationDB
import com.bestDate.db.entity.UserDB
import java.text.SimpleDateFormat
import java.util.Calendar.*

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
) : BaseResponse()

data class ProfileImage(
    var id: Int? = null,
    var full_url: String? = null,
    var thumb_url: String? = null,
    var main: Boolean? = null,
    var top: Boolean? = null,
    var top_place: Int? = null,
    var liked: Boolean? = null,
    var likes: Int? = null
) {
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

data class ShortUserDataResponse(
    val data: ShortUserData
) : BaseResponse()

data class ShortUserListDataResponse(
    val data: MutableList<ShortUserData>
) : BaseResponse()

@Entity
data class ShortUserData(
    var id: Int? = null,
    var name: String? = null,
    var gender: String? = null,
    var language: String? = null,
    var birthday: String? = null,
    var role: String? = null,
    var blocked: Boolean? = null,
    var allow_chat: Boolean? = null,
    var is_online: Boolean? = null,
    var last_online_at: String? = null,
    @Embedded
    var main_photo: ProfileImage? = null,
    @Embedded
    var location: LocationDB? = null
    var block_messages: Boolean? = null,
    var full_questionnaire: Boolean? = null,
    var distance: Double? = null
) {
    fun getAge(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val yourDate = birthday?.let { sdf.parse(it) } ?: return ""
        return CalendarUtils.getDiffYears(yourDate, getInstance().time).toString()
    }
}

data class FilterOptions(
    val location: String = "all",
    val online: String = "all"
)

data class Like(
    val id: Int? = null,
    val created_at: String? = null,
    val photo: ProfileImage? = null,
    val user: ShortUserData? = null
) {
    fun getLikeTime(): String {
        return ""
    }
}