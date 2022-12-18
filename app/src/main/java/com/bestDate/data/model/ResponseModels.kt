package com.bestDate.data.model

import android.content.Context
import android.os.Parcelable
import com.bestDate.data.extension.*
import com.bestDate.db.entity.Invitation
import com.bestDate.db.entity.LocationDB
import com.bestDate.db.entity.UserDB
import com.bestDate.db.entity.UserSettings
import kotlinx.parcelize.Parcelize
import java.util.*

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

@Parcelize
data class ProfileImage(
    var id: Int? = null,
    var full_url: String? = null,
    var thumb_url: String? = null,
    var main: Boolean? = null,
    var top: Boolean? = null,
    var top_place: Int? = null,
    var liked: Boolean? = null,
    var likes: Int? = null,
    var created_at: String? = null
) : Parcelable {
    fun copy(): ProfileImage {
        return ProfileImage(id, full_url, thumb_url, main, top, top_place, liked, likes)
    }

    fun getDefaultPhoto() = ProfileImage(
        full_url = "https://as2.ftcdn.net/jpg/03/46/93/61/220_F_346936114_RaxE6OQogebgAWTalE1myseY1Hbb5qPM.jpg",
        thumb_url = "https://as2.ftcdn.net/jpg/03/46/93/61/220_F_346936114_RaxE6OQogebgAWTalE1myseY1Hbb5qPM.jpg"
    )
}

data class UserDataResponse(
    val data: UserDB
) : BaseResponse()

data class LikesListResponse(
    val data: MutableList<Like>
) : BaseResponse()

data class MatchesListResponse(
    val data: MutableList<Match>
) : BaseResponse()

data class MyDuelsResponse(
    val data: MutableList<MyDuel>
) : BaseResponse()

data class ShortUserDataResponse(
    val data: ShortUserData
) : BaseResponse()

data class ShortUserListDataResponse(
    val data: MutableList<ShortUserData>? = null,
    val meta: Meta? = null
) : BaseResponse()

data class Meta(
    val per_page: Int? = null,
    var total: Int? = null,
    var current_page: Int? = null,
    var last_page: Int? = null
)

data class InvitationsListResponse(
    val data: MutableList<Invitation>
) : BaseResponse()

data class UserInvitationsResponse(
    val data: MutableList<InvitationCard>
) : BaseResponse()

data class UserSettingsResponse(
    val data: UserSettings
) : BaseResponse()

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
) : Parcelable {
    fun getMainPhoto(): ProfileImage {
        return main_photo ?: ProfileImage().getDefaultPhoto()
    }

    fun getAge(): String {
        val yourDate = birthday?.let { it.getDateWithTimeOffset() } ?: return "18"
        return getDiffYears(yourDate, Date()).toString()
    }

    fun getLocation(): String {
        return "${location?.country.orEmpty()}, ${location?.city.orEmpty()}"
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

data class MyDuel(
    val id: Int? = null,
    val created_at: String? = null,
    val winning_photo: ProfileImage? = null,
    val loser_photo: ProfileImage? = null,
    val voter: ShortUserData? = null
) {
    fun getVisitPeriod(context: Context): String {
        return created_at.getVisitPeriod(context)
    }
}

data class GuestsResponse(
    val data: MutableList<Guest>? = null
) : BaseResponse()

data class Guest(
    val id: Int? = null,
    val visit_at: String? = null,
    val viewed: Boolean? = null,
    val guest: ShortUserData? = null
)


data class InvitationCard(
    val id: Int? = null,
    var invitation: Invitation? = null,
    var from_user: ShortUserData? = null,
    var to_user: ShortUserData? = null,
    var answer: Invitation? = null,
    var created_at: String? = null
) {
    fun getAnswer(): InvitationAnswer {
        return when (answer?.id) {
            1 -> InvitationAnswer.YES
            2 -> InvitationAnswer.YES_NEXT_TIME
            3 -> InvitationAnswer.NOT_YET
            4 -> InvitationAnswer.NO
            else -> InvitationAnswer.NONE
        }
    }
}