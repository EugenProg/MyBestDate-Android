package com.bestDate.presentation.main

import com.bestDate.data.model.InternalException
import com.bestDate.db.dao.UserDao
import com.bestDate.db.entity.UserDB
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUseCase @Inject constructor(
    private val userDao: UserDao,
    private val userRemoteData: UserRemoteData) {

    val getMyUser = userDao.getUserFlow()

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
}