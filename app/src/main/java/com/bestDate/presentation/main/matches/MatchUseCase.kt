package com.bestDate.presentation.main.matches

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.Match
import com.bestDate.data.model.ShortUserData
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchUseCase @Inject constructor(
    private val remoteData: UserRemoteData
) {

    val matchesList: MutableLiveData<MutableList<ShortUserData>> = MutableLiveData(mutableListOf())
    val matchAction: MutableLiveData<Match?> = MutableLiveData()

    var fullList: MutableList<ShortUserData> = mutableListOf()
    private val pageSize: Int = 10

    suspend fun getUsersForMatch() {
        val response = remoteData.getUsersForMatch()
        if (response.isSuccessful) {
            response.body()?.data?.let {
                fullList = it
                setCurrentPage()
            }
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun matchAction(userId: Int) {
        val response = remoteData.matchAction(userId)
        if (response.isSuccessful) {
            response.body()?.data?.let {
                if (it.matched == true) matchAction.postValue(it)
            }
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    private fun setCurrentPage() {
        val list: MutableList<ShortUserData> = mutableListOf()
        val endIndex = if (pageSize > fullList.size) fullList.lastIndex else pageSize

        for (index in 0..endIndex) {
            list.add(fullList[index])
        }
        matchesList.postValue(list)


        fullList = if (endIndex >= fullList.lastIndex) mutableListOf()
                    else fullList.subList(endIndex + 1, fullList.lastIndex)
    }

    fun nextUser() {
        matchesList.value?.removeLast()

        if (fullList.isNotEmpty() && matchesList.value.isNullOrEmpty()) setCurrentPage()
    }

    fun clearMatch() {
        matchAction.value = null
    }

    fun clearData() {
        fullList = mutableListOf()
        matchesList.postValue(mutableListOf())
    }
}