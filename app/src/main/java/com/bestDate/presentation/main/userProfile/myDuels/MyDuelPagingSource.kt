package com.bestDate.presentation.main.userProfile.myDuels

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.MyDuel
import com.bestDate.network.remote.UserRemoteData

class MyDuelPagingSource(private var remoteDate: UserRemoteData): PagingSource<Int, MyDuel>() {
    override fun getRefreshKey(state: PagingState<Int, MyDuel>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MyDuel> {
        return try {
            val response = remoteDate.getMyDuels(params.key ?: 1).body()
            val nextPage = if (response?.meta?.current_page.orZero < response?.meta?.last_page.orZero) {
                response?.meta?.current_page.orZero + 1
            } else null
            LoadResult.Page(
                data = response?.data ?: mutableListOf(),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}