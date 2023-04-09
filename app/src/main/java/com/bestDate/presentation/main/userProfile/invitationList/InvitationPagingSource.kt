package com.bestDate.presentation.main.userProfile.invitationList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.InvitationCard
import com.bestDate.data.model.InvitationFilter
import com.bestDate.network.remote.UserRemoteData

class InvitationPagingSource(private val remoteData: UserRemoteData,
                             private val type: InvitationFilter):
    PagingSource<Int, InvitationCard>() {
    override fun getRefreshKey(state: PagingState<Int, InvitationCard>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InvitationCard> {
        return try {
            val response = remoteData.getUserInvitations(type, params.key ?: 1).body()
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