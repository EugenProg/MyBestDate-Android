package com.bestDate.presentation.main.userProfile.myDuels

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyDuelsUseCase @Inject constructor(
    private val userRemoteData: UserRemoteData
) {

    var myDuels = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false, initialLoadSize = 1),
        pagingSourceFactory = { MyDuelPagingSource(userRemoteData) }
    ).flow
}