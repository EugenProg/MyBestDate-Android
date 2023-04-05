package com.bestDate.presentation.main.userProfile.likesList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.Like
import com.bestDate.network.remote.UserRemoteData

class LikesPagingSource(private val remoteData: UserRemoteData): PagingSource<Int, Like>() {
    override fun getRefreshKey(state: PagingState<Int, Like>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Like> {
        return try {
            val response = remoteData.getUserLikes(params.key ?: 1).body()
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