package com.bestDate.presentation.main.duels.top

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.DuelProfile
import com.bestDate.network.remote.TopRemoteData
import com.bestDate.presentation.registration.Gender

class TopPagingSource(private val remoteData: TopRemoteData, val gender: Gender) :
    PagingSource<Int, DuelProfile>() {
    override fun getRefreshKey(state: PagingState<Int, DuelProfile>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DuelProfile> {
        return try {
            val response = remoteData.getTop(gender.serverName, params.key ?: 1).body()
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