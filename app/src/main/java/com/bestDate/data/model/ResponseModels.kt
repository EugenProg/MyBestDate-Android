package com.bestDate.data.model

import android.content.Context
import android.os.Parcelable
import com.bestDate.R
import com.bestDate.data.extension.*
import com.bestDate.db.entity.Invitation
import com.bestDate.db.entity.LocationDB
import com.bestDate.db.entity.UserDB
import com.bestDate.db.entity.UserSettings
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.*

open class BaseResponse {
    var success: Boolean = false
    var message: String = ""
}

data class AuthResponse(
    var token_type: String? = null,
    var expires_in: Int? = null,
    var access_token: String? = null,
    var refresh_token: String? = null,
    var registration: Boolean? = null
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
        full_url = "https://dev-api.bestdate.info/images/default_photo.jpg",
        thumb_url = "https://dev-api.bestdate.info/images/default_photo.jpg"
    )
}

data class UserDataResponse(
    val data: UserDB
) : BaseResponse()

data class LikesListResponse(
    val data: MutableList<Like>,
    val meta: Meta? = null
) : BaseResponse()

data class LikeResponse(
    val data: ProfileImage
) : BaseResponse()

data class MatchesListResponse(
    val data: MutableList<Match>,
    val meta: Meta? = null
) : BaseResponse()

data class MatchActionResponse(
    val data: Match? = null
) : BaseResponse()

data class MyDuelsResponse(
    val data: MutableList<MyDuel>,
    val meta: Meta? = null
) : BaseResponse()

data class ShortUsersDataResponse(
    val data: MutableList<ShortUserData>
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
    val data: MutableList<InvitationCard>? = null,
    val meta: Meta? = null
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
    var block_messages: Boolean? = null,
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

    fun isBot() = role == "bot"
}

data class FilterOptions(
    val location: String? = "all",
    val online: String? = "all",
    val filters: AdditionalFilters? = null
)

data class AdditionalFilters(
    val location: LocationParams? = null
)

data class LocationParams(
    val range: Int? = null,
    val lat: String? = null,
    val lng: String? = null
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
    val data: MutableList<Guest>? = null,
    val meta: Meta? = null
) : BaseResponse()

data class Guest(
    val id: Int? = null,
    val visit_at: String? = null,
    val viewed: Boolean? = null,
    val guest: ShortUserData? = null,
    val itemType: ListItemType? = null
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

data class GeocodingResponse(
    var place_id: Int? = null,
    var licence: String? = null,
    var osm_type: String? = null,
    var lat: String? = null,
    var lon: String? = null,
    var display_name: String? = null,
    var address: Address? = null
) {
    fun getSaveUserRequest(): SaveUserLocationRequest {
        return SaveUserLocationRequest(
            lat = lat.orEmpty(),
            lng = lon.orEmpty(),
            iso_code = address?.country_code.orEmpty(),
            country = address?.country.orEmpty(),
            state = address?.country_code,
            state_name = address?.state_district,
            city = address?.city ?: address?.town.orEmpty()
        )
    }
}

data class Address(
    var city: String? = null,
    var town: String? = null,
    var county: String? = null,
    var state_district: String? = null,
    var state: String? = null,
    var country: String? = null,
    var country_code: String? = null
)

data class DuelProfileImageListResponse(
    var data: MutableList<ProfileImage>? = null
) : BaseResponse()

data class DuelProfileResponse(
    val data: MutableList<DuelProfile>? = null,
    val meta: Meta? = null
) : BaseResponse()

data class DuelProfile(
    val id: Int? = null,
    var full_url: String? = null,
    var thumb_url: String? = null,
    val rating: BigDecimal? = null,
    val user: ShortUserData? = null,
    val location: LocationDB? = null
)

data class ChatListResponse(
    var data: MutableList<Chat>? = mutableListOf(),
    val meta: Meta? = null
) : BaseResponse()

data class Chat(
    var id: Int? = null,
    var user: ShortUserData? = null,
    var last_message: Message? = null,
    var type: ListItemType? = null,
    var typingMode: Boolean? = null
) {
    fun transform(itemType: ListItemType): Chat {
        return Chat(
            user?.id,
            user,
            last_message,
            if (user?.isBot() == true) ListItemType.BOT else itemType
        )
    }

    fun getLastMessageTime(): String {
        val created = last_message?.created_at
        return if (created.isToday()) created.getTime()
        else {
            val date = created.getDateWithTimeOffset()
            val days = getDaysBetween(Date(), date)
            if (days > 6) date.toShortDate()
            else date.toWeekday()
        }
    }
}

enum class ListItemType {
    HEADER, NEW_ITEM, OLD_ITEM, BOT, LOADER
}

enum class BackScreenType {
    ANOTHER_PROFILE, CHAT, CHAT_LIST, MATCHES, SEARCH, PROFILE, GUESTS, DUELS, START
}

data class Message(
    var id: Int? = null,
    var sender_id: Int? = null,
    var recipient_id: Int? = null,
    var parent_id: Int? = null,
    var text: String? = null,
    var image: ChatImage? = null,
    var read_at: String? = null,
    var created_at: String? = null,
    var parentMessage: ParentMessage? = null,
    var isLastMessage: Boolean? = null,
    var viewType: ChatItemType? = null
) {
    fun transform(type: ChatItemType, parent: ParentMessage?, isLast: Boolean?): Message {
        return Message(
            id, sender_id, recipient_id, parent_id, text, image, read_at, created_at,
            parent, isLast, type
        )
    }

    fun getDate(context: Context): String {
        val dateBetween = getDaysBetween(created_at.getDateWithTimeOffset(), Date())
        return when {
            created_at.isToday() -> context.getString(R.string.today)
            dateBetween == 1 -> context.getString(R.string.yesterday)
            else -> created_at.toShortDate()
        }
    }
}

data class ParentMessage(
    var id: Int? = null,
    var text: String? = null,
    var image: ChatImage? = null
)

enum class ChatItemType {
    DATE, MY_TEXT_MESSAGE, USER_TEXT_MESSAGE, MY_IMAGE_MESSAGE, USER_IMAGE_MESSAGE, LOADER
}

data class ChatImage(
    var id: Int? = null,
    var full_url: String? = null,
    var thumb_url: String? = null
)

data class ChatMessagesResponse(
    val data: MutableList<Message>? = mutableListOf(),
    val meta: Meta? = null
) : BaseResponse()

data class SendMessageResponse(
    val data: Message? = null
) : BaseResponse()

data class TranslationResponse(
    var translations: MutableList<Translation> = mutableListOf()
)

data class Translation(
    var detected_source_language: String? = null,
    var text: String? = null
)

data class PusherMessageResponse(
    val message: Message? = null
)

data class PusherCoinsResponse(
    var id: Int? = null,
    val coins: String? = null
)

data class PusherReadingResponse(
    var id: Int? = null,
    var last_message: Message? = null
)

data class PusherTypingResponse(
    var id: Int? = null,
    var sender_id: Int? = null
)

data class GoogleAccessTokenResponse(
    var access_token: String? = null,
    var expires_in: Int? = null,
    var refresh_token: String? = null,
    var scope: String? = null,
    var token_type: String? = null,
    var id_token: String? = null
)