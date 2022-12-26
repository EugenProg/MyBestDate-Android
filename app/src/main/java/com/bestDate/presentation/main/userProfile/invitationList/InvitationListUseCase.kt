package com.bestDate.presentation.main.userProfile.invitationList

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.InvitationAnswer
import com.bestDate.data.model.InvitationCard
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

    var newInvitations: MutableLiveData<MutableList<InvitationCard>> = MutableLiveData()
    var answeredInvitations: MutableLiveData<MutableList<InvitationCard>> = MutableLiveData()
    var sentInvitations: MutableLiveData<MutableList<InvitationCard>> = MutableLiveData()

    suspend fun refreshNewInvitations() {
        val response = userRemoteData.getUserInvitations(InvitationFilter.NEW)
        if (response.isSuccessful) {
            response.body()?.let {
                newInvitations.postValue(it.data)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun refreshAnsweredInvitations() {
        val response = userRemoteData.getUserInvitations(InvitationFilter.ANSWERED)
        if (response.isSuccessful) {
            response.body()?.let {
                answeredInvitations.postValue(it.data)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun refreshSentInvitations() {
        val response = userRemoteData.getUserInvitations(InvitationFilter.SENT)
        if (response.isSuccessful) {
            response.body()?.let {
                sentInvitations.postValue(it.data)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    suspend fun answerToInvitation(invitationId: Int?, answer: InvitationAnswer) {
        val response = invitationRemoteData.answerToInvitation(invitationId, answer)
        if (!response.isSuccessful) throw InternalException.OperationException(
            response.errorBody()?.getErrorMessage()
        )
    }

    fun clearData() {
        newInvitations.postValue(mutableListOf())
        answeredInvitations.postValue(mutableListOf())
        sentInvitations.postValue(mutableListOf())
    }
}