package com.bestDate.network.remote

import com.bestDate.data.model.*
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.network.services.CoreAuthService
import javax.inject.Inject

class AuthRemoteData @Inject constructor(
    private val service: CoreAuthService,
    private val preferencesUtils: PreferencesUtils
) {
    suspend fun loginByEmail(email: String, password: String) =
        service.loginByEmail(EmailAuthRequest(username = email, password))

    suspend fun loginByPhone(phone: String, password: String) =
        service.loginByPhone(PhoneAuthRequest(phone, password))

    suspend fun loginSocial(provider: SocialProvider) =
        service.loginSocial(SocialAuthRequest(provider.serverName, preferencesUtils.getString(
            Preferences.ACCESS_TOKEN)))

    suspend fun refreshToken() =
        service.refreshToken(RefreshRequest(preferencesUtils.getString(Preferences.ACCESS_TOKEN)))

    suspend fun logout() = service.logout(preferencesUtils.getString(Preferences.ACCESS_TOKEN))

    suspend fun sendEmailResetCode(email: String) =
        service.sendEmailResetCode(EmailRequest(email))

    suspend fun resetEmailPassword(email: String, code: String, password: String) =
        service.resetEmailPassword(
            ConfirmRequest(
                email = email,
                code = code,
                password = password,
                password_confirmation = password
            )
        )

    suspend fun sendPhoneResetCode(phone: String) =
        service.sendPhoneResetCode(PhoneRequest(phone))

    suspend fun resetPhonePassword(phone: String, code: String, password: String) =
        service.resetPhonePassword(
            ConfirmRequest(
                phone = phone,
                code = code,
                password = password,
                password_confirmation = password
            )
        )

    suspend fun sendEmailCode(email: String) =
        service.sendEmailCode(EmailRequest(email))

    suspend fun confirmEmailCode(email: String, code: String) =
        service.confirmEmailCode(ConfirmRequest(email = email, code = code))

    suspend fun createUserByEmail(registration: RegistrationRequest) =
        service.registerByEmail(registration)

    suspend fun sendPhoneCode(phone: String) =
        service.sendPhoneCode(PhoneRequest(phone))

    suspend fun confirmPhoneCode(phone: String, code: String) =
        service.confirmPhoneCode(ConfirmRequest(phone = phone, code = code))

    suspend fun createUserByPhone(registration: RegistrationRequest) =
        service.registerByPhone(registration)
}