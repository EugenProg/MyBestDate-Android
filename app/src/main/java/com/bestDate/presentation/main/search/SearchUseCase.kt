package com.bestDate.presentation.main.search

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.FilterOptions
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.ShortUserData
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData
) {

    var usersList: MutableLiveData<MutableList<ShortUserData>?> = MutableLiveData(mutableListOf())
    var perPage: Int = 10
    var currentPage: Int = 1
    var lastPage: Int = 1
    var total: Int = 0

    suspend fun getUsers(filters: FilterOptions) {
        val response = userRemoteData.getUsers(filters, 1)
        if (response.isSuccessful) {
            usersList.postValue(response.body()?.data)
            perPage = response.body()?.meta?.per_page ?: 0
            currentPage = response.body()?.meta?.current_page ?: 0
            lastPage = response.body()?.meta?.last_page ?: 0
            total = response.body()?.meta?.total ?: 0
        } else {
            throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
        }
    }

    suspend fun getUsersPaged(filters: FilterOptions) {
        currentPage++

        if (lastPage < currentPage) return
        val response = userRemoteData.getUsers(filters, currentPage)
        if (response.isSuccessful) {
            val list = mutableListOf<ShortUserData>()
            usersList.value.let { list.addAll(it.orEmpty()) }
            list.addAll(response.body()?.data ?: mutableListOf())
            usersList.postValue(list)
        } else {
            throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
        }
    }

    fun clearPagingData() {
        usersList.postValue(mutableListOf())
        perPage = 10
        currentPage = 1
        lastPage = 1
        total = 0
    }
}