package com.bestDate.presentation.main.userProfile.invitationList

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.InvitationAnswer
import com.bestDate.data.model.InvitationFilter
import com.bestDate.network.remote.InvitationsRemoteData
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvitationListUseCase @Inject constructor(
    private var userRemoteData: UserRemoteData,
    private var invitationRemoteData: InvitationsRemoteData
) {
    var hasNewInvitations: MutableLiveData<Boolean> = MutableLiveData(false)

    var newInvitations = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false, initialLoadSize = 1),
        pagingSourceFactory = {
            val source = InvitationPagingSource(userRemoteData, InvitationFilter.NEW)

            source.hasNewInvitations = { hasNewInvitations.postValue(it) }
            source
        }
    ).flow

    var answeredInvitations = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false, initialLoadSize = 1),
        pagingSourceFactory = { InvitationPagingSource(userRemoteData, InvitationFilter.ANSWERED) }
    ).flow

    var sentInvitations = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false, initialLoadSize = 1),
        pagingSourceFactory = { InvitationPagingSource(userRemoteData, InvitationFilter.SENT) }
    ).flow

    suspend fun answerToInvitation(invitationId: Int?, answer: InvitationAnswer) {
        val response = invitationRemoteData.answerToInvitation(invitationId, answer)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }
}