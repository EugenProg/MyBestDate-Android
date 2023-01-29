package com.bestDate.presentation.main.userProfile.matchesList

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.Match
import com.bestDate.db.dao.UserDao
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchesListUseCase @Inject constructor(
    private val userDao: UserDao,
    private val userRemoteData: UserRemoteData
) {

    val matchesList: MutableLiveData<Pair<MutableList<Match>, String>> = MutableLiveData()

    suspend fun getMatches() {
        val myPhoto = userDao.getUser()?.getMainPhotoThumbUrl().orEmpty()
        val response = userRemoteData.getUserMatches()
        if (response.isSuccessful) {
            response.body()?.let {
                matchesList.postValue(Pair(it.data, myPhoto))
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    fun clearData() {
        matchesList.postValue(Pair(mutableListOf(), ""))
    }
}