package com.bestDate.network.services

import com.bestDate.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface CoreAuthService {
    /**Login by email*/
    @Headers("isAuthorize: false")
    @POST("/api/v1/login-email")
    suspend fun loginByEmail(@Body request: EmailAuthRequest): Response<AuthResponse>

    /**Login by phone*/
    @Headers("isAuthorize: false")
    @POST("/api/v1/login-phone")
    suspend fun loginByPhone(@Body request: PhoneAuthRequest): Response<AuthResponse>

    /**login social*/
    @Headers("isAuthorize: false")
    @POST("/api/v1/login-social")
    suspend fun loginSocial(@Body request: SocialAuthRequest): Response<AuthResponse>

    /**refresh token*/
    @Headers("isAuthorize: false")
    @GET("/api/v1/refresh-token")
    suspend fun refreshToken(@Body request: RefreshRequest): Response<AuthResponse>

    /**logout*/
    @GET("/api/v1/logout")
    suspend fun logout(): Response<BaseResponse>

    /**send code for reset password by email*/
    @Headers("isAuthorize: false")
    @POST("/api/v1/email/password-reset-send-code")
    suspend fun sendEmailResetCode(@Body email: EmailRequest): Response<BaseResponse>

    /**reset password by email*/
    @Headers("isAuthorize: false")
    @POST("/api/v1/email/password-reset-by-code")
    suspend fun resetEmailPassword(@Body confirmRequest: ConfirmRequest): Response<BaseResponse>

    /**send code for reset password by phone*/
    @Headers("isAuthorize: false")
    @POST("/api/v1/phone/password-reset-send-code")
    suspend fun sendPhoneResetCode(@Body phone: PhoneRequest): Response<BaseResponse>

    /**reset password by phone*/
    @Headers("isAuthorize: false")
    @POST("/api/v1/phone/password-reset-by-code")
    suspend fun resetPhonePassword(@Body request: ConfirmRequest): Response<BaseResponse>

    /**send email code fo registration*/
    @Headers("isAuthorize: false")
    @POST("/api/v1/email/send-code")
    suspend fun sendEmailCode(@Body email: EmailRequest): Response<BaseResponse>

    /**confirm email with code*/
    @Headers("isAuthorize: false")
    @POST("/api/v1/email/confirm-code")
    suspend fun confirmEmailCode(@Body request: ConfirmRequest): Response<BaseResponse>

    /**create user by email*/
    @Headers("isAuthorize: false")
    @POST("/api/v1/email/register")
    suspend fun registerByEmail(@Body request: RegistrationRequest): Response<BaseResponse>

    /**send email code fo registration*/
    @Headers("isAuthorize: false")
    @POST("/api/v1/phone/send-code")
    suspend fun sendPhoneCode(@Body phone: PhoneRequest): Response<BaseResponse>

    /**confirm phone with code*/
    @Headers("isAuthorize: false")
    @POST("/api/v1/phone/confirm-code")
    suspend fun confirmPhoneCode(@Body confirmRequest: ConfirmRequest): Response<BaseResponse>

    /**create user by phone*/
    @Headers("isAuthorize: false")
    @POST("/api/v1/phone/register")
    suspend fun registerByPhone(@Body request: RegistrationRequest): Response<BaseResponse>
}