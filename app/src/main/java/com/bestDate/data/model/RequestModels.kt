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