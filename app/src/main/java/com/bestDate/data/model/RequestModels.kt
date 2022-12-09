package com.bestDate.data.model

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
    var ids: MutableList<Int>
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