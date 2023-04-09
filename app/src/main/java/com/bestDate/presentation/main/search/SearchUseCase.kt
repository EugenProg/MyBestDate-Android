package com.bestDate.presentation.main.search

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.FilterOptions
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.Meta
import com.bestDate.data.model.ShortUserData
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData
) {
    var usersList: MutableLiveData<MutableList<ShortUserData>?> = MutableLiveData(mutableListOf())
    var meta: Meta? = Meta()

    suspend fun getUsers(filters: FilterOptions) {
        val response = userRemoteData.getUsers(filters, 1)
        if (response.isSuccessful) {
            response.body()?.let {
                usersList.postValue(it.data)
                meta = it.meta
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun getUsersPaged(filters: FilterOptions) {
        if (meta?.last_page.orZero < meta?.current_page.orZero) return
        val response = userRemoteData.getUsers(filters, meta?.current_page.orZero + 1)
        if (response.isSuccessful) {
            response.body()?.let {
                val list = mutableListOf<ShortUserData>()
                usersList.value.let { users -> list.addAll(users.orEmpty()) }
                list.addAll(it.data ?: mutableListOf())
                meta = it.meta
                usersList.postValue(list)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    fun clearPagingData() {
        usersList.postValue(mutableListOf())
        meta = Meta()
    }
}