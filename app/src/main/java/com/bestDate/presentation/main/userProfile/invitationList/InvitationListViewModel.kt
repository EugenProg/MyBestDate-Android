package com.bestDate.presentation.main.userProfile.invitationList

import androidx.lifecycle.MutableLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.data.model.InvitationAnswer
import com.bestDate.data.model.InvitationCard
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvitationListViewModel @Inject constructor(
    private val invitationListUseCase: InvitationListUseCase
): BaseViewModel() {

    var newInvitations: MutableLiveData<MutableList<InvitationCard>> =
        invitationListUseCase.newInvitations
    var answeredInvitations: MutableLiveData<MutableList<InvitationCard>> =
        invitationListUseCase.answeredInvitations
    var sentInvitations: MutableLiveData<MutableList<InvitationCard>> =
        invitationListUseCase.sentInvitations

    fun refreshNewInvitationList() {
        doAsync {
            invitationListUseCase.refreshNewInvitations()
        }
    }

    fun refreshAnsweredInvitationList() {
        doAsync {
            invitationListUseCase.refreshAnsweredInvitations()
        }
    }

    fun refreshSentInvitationList() {
        doAsync {
            invitationListUseCase.refreshSentInvitations()
        }
    }

    fun answerToInvitation(answer: InvitationAnswer, invitationId: Int?) {
        doAsync {
            invitationListUseCase.answerToInvitation(invitationId, answer)
            invitationListUseCase.refreshNewInvitations()
            refreshAnsweredInvitationList()
        }
    }
}