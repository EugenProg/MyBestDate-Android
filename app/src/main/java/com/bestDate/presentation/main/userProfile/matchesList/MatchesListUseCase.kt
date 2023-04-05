package com.bestDate.presentation.main.userProfile.matchesList

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchesListUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData
) {
    val matchesList = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false, initialLoadSize = 1),
        pagingSourceFactory = { MatchesPagingSource(userRemoteData) }
    ).flow
}