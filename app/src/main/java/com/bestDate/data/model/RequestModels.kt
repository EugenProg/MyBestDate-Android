package com.bestDate.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.bestDate.R

data class EmailAuthRequest(
    val username: String,
    val password: String,
    val grant_type: String = "password"
)

data class PhoneAuthRequest(
    val phone: String,
    val password: String,
    val grant_type: String = "phone"
)

data class SocialAuthRequest(
    val provider: String,
    val access_token: String,
    val grant_type: String = "social"
)

enum class SocialProvider(var serverName: String) {
    GOOGLE("google"),
    FACEBOOK("facebook")
}

data class RefreshRequest(
    val refresh_token: String,
    val grant_type: String = "refresh_token"
)

data class EmailRequest(
    val email: String
)

data class PhoneRequest(
    val phone: String
)

data class ConfirmRequest(
    val email: String? = null,
    val phone: String? = null,
    val code: String,
    val password: String? = null,
    val password_confirmation: String? = null
)

data class RegistrationRequest(
    var email: String? = null,
    var phone: String? = null,
    var name: String,
    var password: String,
    var password_confirmation: String,
    var gender: String,
    var language: String = "en",
    var birthday: String,
    var look_for: MutableList<String>
)

data class IdListRequest(
    var ids: MutableList<Int?>
)

data class PhotoStatusUpdateRequest(
    var main: Boolean,
    var top: Boolean,
    var match: Boolean = false
)

data class RequestLanguage(
    var language: String
)

data class SendInvitationRequest(
    var invitation_id: Int,
    var user_id: Int
)

data class InvitationAnswerRequest(
    var answer_id: Int
)

data class UserInvitationRequest(
    var filter: String
)

data class UpdateUserRequest(
    var name: String?,
    var gender: String?,
    var birthday: String?,
    var look_for: MutableList<String>?
)

data class UpdatePasswordRequest(
    var old_password: String,
    var password: String,
    var password_confirmation: String,
)

data class UpdateSettingsRequest(
    var block_messages: Boolean? = null,
    var matches: Boolean? = null,
    var likes_notifications: Boolean? = null,
    var matches_notifications: Boolean? = null,
    var invitations_notifications: Boolean? = null,
    var messages_notifications: Boolean? = null,
    var guests_notifications: Boolean? = null
)

enum class SettingsType {
    MESSAGES, NOTIFY_LIKES, NOTIFY_MATCHES, NOTIFY_INVITATION, NOTIFY_MESSAGES, NOTIFY_GUESTS, MATCHES;

    fun getSettingsRequest(checked: Boolean): UpdateSettingsRequest {
        return when (this) {
            MESSAGES -> UpdateSettingsRequest(block_messages = checked)
            NOTIFY_LIKES -> UpdateSettingsRequest(likes_notifications = checked)
            NOTIFY_MATCHES -> UpdateSettingsRequest(matches_notifications = checked)
            NOTIFY_INVITATION -> UpdateSettingsRequest(invitations_notifications = checked)
            NOTIFY_MESSAGES -> UpdateSettingsRequest(messages_notifications = checked)
            NOTIFY_GUESTS -> UpdateSettingsRequest(guests_notifications = checked)
            MATCHES -> UpdateSettingsRequest(matches = checked)
        }
    }
}

data class SaveUserLocationRequest(
    var lat: String,
    var lng: String,
    var iso_code: String,
    var country: String,
    var state: String?,
    var state_name: String?,
    var city: String
)

enum class InvitationAnswer(var id: Int, @StringRes var title: Int, @DrawableRes val button: Int) {
    YES(1, R.string.yes_i_agree, R.drawable.positive_answer_btn),
    YES_NEXT_TIME(2, R.string.yes_i_will_but_next_time, R.drawable.positive_answer_btn),
    NO(3, R.string.no, R.drawable.negative_answer_btn),
    NOT_YET(4, R.string.thanks_but_i_cant_yet, R.drawable.negative_answer_btn),
    NONE(0, R.string.no, R.drawable.negative_answer_btn)
}

enum class InvitationFilter(var serverName: String) {
    NEW("new"),
    ANSWERED("answered"),
    SENT("sent")
}

data class LikesBody(
    var photo_id: Int
)

data class DuelRequest(
    val gender: String,
    val country: String? = null
)

data class DuelVoteRequest(
    val winning_photo: Int,
    val loser_photo: Int
)

data class SendMessageRequest(
    val text: String
)

data class SaveDeviceTokenRequest(
    val type: String = "android",
    val token: String
)

data class MatchActionRequest(
    var user_id: Int
)

data class GetGoogleAccessTokenRequest(
    val grant_type: String = "authorization_code",
    val client_id: String = "320734338059-qfih51slhp529h06gplp1r09f4li41vm.apps.googleusercontent.com",
    val client_secret: String = "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",
    val code: String
)

data class SubscriptionInfoRequest(
    val device: String = "android",
    val sub_id: String? = null,
    val start_at: String? = null,
    val end_at: String? = null
)

data class WithdrawCoinsRequest(
    var source: String
)