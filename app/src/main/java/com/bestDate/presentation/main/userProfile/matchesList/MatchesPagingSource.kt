package com.bestDate.presentation.main.userProfile.matchesList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.Match
import com.bestDate.network.remote.UserRemoteData

class MatchesPagingSource(private val remoteData: UserRemoteData) : PagingSource<Int, Match>() {
    override fun getRefreshKey(state: PagingState<Int, Match>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Match> {
        return try {
            val response = remoteData.getUserMatches(params.key ?: 1).body()
            val nextPage =
                if (response?.meta?.current_page.orZero < response?.meta?.last_page.orZero) {
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