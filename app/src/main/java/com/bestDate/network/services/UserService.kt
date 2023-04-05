package com.bestDate.network.services

import com.bestDate.data.model.*
import com.bestDate.db.entity.QuestionnaireDB
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    /**Get user data*/
    @GET("/api/v1/user")
    suspend fun getUserData(): Response<UserDataResponse>

    /**Get user by id*/
    @GET("/api/v1/users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<UserDataResponse>

    /**Save questionnaire*/
    @PUT("/api/v1/user/questionnaire")
    suspend fun saveQuestionnaire(@Body questionnaire: QuestionnaireDB): Response<BaseResponse>

    /**Get user likes list*/
    @GET("/api/v1/likes")
    suspend fun getLikesList(@Query("page") page: Int): Response<LikesListResponse>

    /**Post like*/
    @POST("/api/v1/likes")
    suspend fun like(@Body body: LikesBody): Response<LikeResponse>

    /**Get user matches list*/
    @GET("/api/v1/match")
    suspend fun getMatchesList(@Query("page") page: Int): Response<MatchesListResponse>

    /**Get users for match*/
    @GET("/api/v1/match-users")
    suspend fun getUsersForMatch(): Response<ShortUsersDataResponse>

    /**Match action*/
    @POST("/api/v1/match")
    suspend fun matchAction(@Body body: MatchActionRequest): Response<MatchActionResponse>

    /**Get user duels*/
    @GET("/api/v1/voting")
    suspend fun getMyDuels(@Query("page") page: Int): Response<MyDuelsResponse>

    /**Get blocked users*/
    @GET("/api/v1/blocked-users")
    suspend fun getBlockedUsers(): Response<ShortUserListDataResponse>

    /**Block user*/
    @POST("/api/v1/block-user/{id}")
    suspend fun blockUser(@Path("id") userId: Int): Response<BaseResponse>

    /**Unlock user*/
    @POST("/api/v1/unlock-user/{id}")
    suspend fun unlockUser(@Path("id") userId: Int): Response<BaseResponse>

    /**Complain*/
    @POST("/api/v1/complaint")
    suspend fun complain(@Body request: MatchActionRequest): Response<BaseResponse>

    /**Change language*/
    @PUT("/api/v1/settings/language")
    suspend fun changeLanguage(@Body body: RequestLanguage): Response<UserDataResponse>

    /**Get users*/
    @POST("/api/v1/users")
    suspend fun getUsers(
        @Body filters: FilterOptions,
        @Query("page") page: Int
    ): Response<ShortUserListDataResponse>

    /**Get user invitations*/
    @POST("/api/v1/user/invitations")
    suspend fun getInvitations(@Body body: UserInvitationRequest): Response<UserInvitationsResponse>

    /**Update user data*/
    @PUT("/api/v1/user")
    suspend fun updateUserData(@Body body: UpdateUserRequest): Response<UserDataResponse>

    /**Send code to email for user*/
    @POST("/api/v1/user/email-code")
    suspend fun sendEmailCode(@Body body: EmailRequest): Response<BaseResponse>

    /**Update user email*/
    @PUT("/api/v1/user/email")
    suspend fun saveUserEmail(@Body body: ConfirmRequest): Response<UserDataResponse>

    /**Send code to phone for user*/
    @POST("/api/v1/user/phone-code")
    suspend fun sendPhoneCode(@Body body: PhoneRequest): Response<BaseResponse>

    /**Update user phone*/
    @PUT("/api/v1/user/phone")
    suspend fun saveUserPhone(@Body body: ConfirmRequest): Response<UserDataResponse>

    /**Change user password*/
    @PUT("/api/v1/user/password")
    suspend fun updateUserPassword(@Body body: UpdatePasswordRequest): Response<BaseResponse>

    /**Update user location*/
    @PUT("/api/v1/user/location")
    suspend fun saveUserLocation(@Body body: SaveUserLocationRequest): Response<UserDataResponse>

    /**Get user settings*/
    @GET("/api/v1/settings")
    suspend fun getUserSettings(): Response<UserSettingsResponse>

    /**update user settings*/
    @PUT("/api/v1/settings")
    suspend fun updateUserSettings(@Body body: UpdateSettingsRequest): Response<UserSettingsResponse>

    /**Delete user profile*/
    @DELETE("/api/v1/user")
    suspend fun deleteUserProfile(): Response<BaseResponse>

    /**Save messaging token*/
    @POST("/api/v1/device-token")
    suspend fun saveMessagingDeviceToken(@Body request: SaveDeviceTokenRequest): Response<BaseResponse>
}