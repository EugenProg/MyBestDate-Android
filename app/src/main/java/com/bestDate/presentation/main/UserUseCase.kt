package com.bestDate.presentation.main

import com.bestDate.data.model.FilterOptions
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.ShortUserData
import com.bestDate.db.dao.UserDao
import com.bestDate.db.entity.QuestionnaireDB
import com.bestDate.db.entity.UserDB
import com.bestDate.network.remote.AuthRemoteData
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUseCase @Inject constructor(
    private val userDao: UserDao,
    private val authRemoteData: AuthRemoteData,
    private val userRemoteData: UserRemoteData
) {

    val getMyUser = userDao.getUserFlow()
    var usersList: MutableList<ShortUserData>? = mutableListOf()
    var perPage: Int = 10
    var currentPage: Int = 1
    var lastPage: Int = 1
    var total: Int = 0

    suspend fun refreshUser() {
        val response = userRemoteData.getUserData()
        if (response.isSuccessful) {
            val user = response.body()?.data
            userDao.validate(user ?: UserDB(id = 0))
        } else throw InternalException.OperationException(response.message())
    }

    suspend fun getUserById(id: Int) {
        val response = userRemoteData.getUserById(id)
        if (response.isSuccessful) {

        } else throw InternalException.OperationException(response.message())
    }

    suspend fun logout() {
        authRemoteData.logout()
        userDao.delete()
    }

    suspend fun saveQuestionnaire(questionnaire: QuestionnaireDB) {
        val response = userRemoteData.saveQuestionnaire(questionnaire)
        if (!response.isSuccessful) throw InternalException.OperationException(response.message())
    }

    suspend fun getUsers(filters: FilterOptions) {
        val response = userRemoteData.getUsers(filters, 1)
        if (response.isSuccessful) {
            usersList = response.body()?.data
            perPage = response.body()?.meta?.per_page ?: 0
            currentPage = response.body()?.meta?.current_page ?: 0
            lastPage = response.body()?.meta?.last_page ?: 0
            total = response.body()?.meta?.total ?: 0
        } else {
            throw InternalException.OperationException(response.message())
        }
    }

    suspend fun getUsersPaged(filters: FilterOptions) {
        currentPage++

        if (lastPage < currentPage) return
        val response = userRemoteData.getUsers(filters, currentPage)
        if (response.isSuccessful) {
            val list = mutableListOf<ShortUserData>()
            usersList?.let { list.addAll(it) }
            list.addAll(response.body()?.data ?: mutableListOf())
            usersList = list
        } else {
            throw InternalException.OperationException(response.message())
        }
    }

    fun clearPagingData() {
        usersList = mutableListOf()
        perPage = 10
        currentPage = 1
        lastPage = 1
        total = 0
    }

    suspend fun changeLanguage(language: String){
        val response = userRemoteData.changeLanguage(language)
        if (response.isSuccessful) {
            val user = response.body()?.data
            userDao.validate(user ?: UserDB(id = 0))
        } else throw InternalException.OperationException(response.message())
    }
}