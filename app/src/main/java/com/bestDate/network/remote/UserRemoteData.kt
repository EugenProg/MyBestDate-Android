package com.bestDate.network.remote

import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.db.entity.QuestionnaireDB
import com.bestDate.network.services.UserService
import javax.inject.Inject

class UserRemoteData @Inject constructor(
    private val service: UserService,
    private val preferencesUtils: PreferencesUtils
) {

    suspend fun getUserData() =
        service.getUserData(preferencesUtils.getString(Preferences.ACCESS_TOKEN))

    suspend fun getUserById(id: Int) =
        service.getUserById(id, preferencesUtils.getString(Preferences.ACCESS_TOKEN))

    suspend fun saveQuestionnaire(questionnaire: QuestionnaireDB) =
        service.saveQuestionnaire(questionnaire, preferencesUtils.getString(Preferences.ACCESS_TOKEN))

    suspend fun getUserLikes() =
        service.getLikesList(preferencesUtils.getString(Preferences.ACCESS_TOKEN))
}