package com.bestDate.presentation.base.anotherProfile

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.ProfileImage
import com.bestDate.db.dao.UserDao
import com.bestDate.db.entity.UserDB
import com.bestDate.network.remote.TranslationRemoteData
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnotherProfileUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData,
    private val userDao: UserDao,
    private val translateRemoteData: TranslationRemoteData
) {

    var user: MutableLiveData<UserDB> = MutableLiveData()
    var photos: MutableLiveData<MutableList<ProfileImage>?> = MutableLiveData()
    var translatedText: String? = null

    suspend fun getUserById(id: Int?) {
        val response = userRemoteData.getUserById(id.orZero)
        if (response.isSuccessful) {
            response.body()?.let {
                user.postValue(it.data)
                photos.postValue(it.data.photos)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun blockUser(id: Int?) {
        val response = userRemoteData.blockUser(id.orZero)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun unlockUser(id: Int?) {
        val response = userRemoteData.unlockUser(id.orZero)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    suspend fun translate() {
        val myUser = userDao.getUser()
        val text = user.value?.questionnaire?.about_me.orEmpty()
        val response = translateRemoteData.translate(text, (myUser?.language ?: "EN").uppercase())
        if (response.isSuccessful) {
            translatedText = response.body()?.translations?.firstOrNull()?.text ?: text
        } else throw InternalException.OperationException(response.message())
    }

    suspend fun complain(id: Int?) {
        val response = userRemoteData.complain(id.orZero)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }
}