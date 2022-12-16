package com.bestDate.presentation.main.userProfile.personalData

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.UpdateUserRequest
import com.bestDate.db.dao.UserDao
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonalDataUseCase @Inject constructor(
    private val userDao: UserDao,
    private val userRemoteData: UserRemoteData
) {

    var getMyUser = userDao.getUserFlow()

    suspend fun updateUserInfo(userRequest: UpdateUserRequest) {
        val response = userRemoteData.updateUserData(userRequest)
        if (response.isSuccessful) {
            response.body()?.data?.let {
                userDao.validate(it)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun sendEmailCode(email: String) {
        val response = userRemoteData.sendEmailCode(email)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun sendPhoneCode(phone: String) {
        val response = userRemoteData.sendPhoneCode(phone)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun saveUserEmail(email: String, code: String) {
        val response = userRemoteData.saveUserEmail(email, code)
        if (response.isSuccessful) {
            response.body()?.data?.let {
                userDao.validate(it)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())

    }

    suspend fun saveUserPhone(phone: String, code: String) {
        val response = userRemoteData.saveUserPhone(phone, code)
        if (response.isSuccessful) {
            response.body()?.data?.let {
                userDao.validate(it)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }
}