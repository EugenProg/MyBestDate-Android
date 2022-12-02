package com.bestDate.network.remote

import com.bestDate.db.entity.QuestionnaireDB
import com.bestDate.network.services.UserService
import javax.inject.Inject

class UserRemoteData @Inject constructor(
    private val service: UserService) {

    suspend fun getUserData() = service.getUserData()

    suspend fun getUserById(id: Int) = service.getUserById(id)

    suspend fun saveQuestionnaire(questionnaire: QuestionnaireDB) =
        service.saveQuestionnaire(questionnaire)

    suspend fun getUserLikes() = service.getLikesList()

    suspend fun getUserMatches() = service.getMatchesList()

    suspend fun getMyDuels() = service.getMyDuels()
}