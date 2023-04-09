package com.bestDate.presentation.main.duels.top

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bestDate.network.remote.TopRemoteData
import com.bestDate.presentation.registration.Gender
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopUseCase @Inject constructor(
    private val topRemoteData: TopRemoteData
) {
    var topsWoman = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false, initialLoadSize = 1),
        pagingSourceFactory = { TopPagingSource(topRemoteData, Gender.WOMAN) }
    ).flow

    var topsMan = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false, initialLoadSize = 1),
        pagingSourceFactory = { TopPagingSource(topRemoteData, Gender.MAN) }
    ).flow
}