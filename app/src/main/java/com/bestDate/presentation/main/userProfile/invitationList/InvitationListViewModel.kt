package com.bestDate.presentation.main.userProfile.invitationList

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bestDate.data.model.InvitationAnswer
import com.bestDate.presentation.base.BaseViewModel
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvitationListViewModel @Inject constructor(
    private val invitationListUseCase: InvitationListUseCase
) : BaseViewModel() {

    private var _answerLiveData: LiveEvent<Boolean> = LiveEvent()
    var answerLiveData: LiveEvent<Boolean> = _answerLiveData

    var newInvitations = invitationListUseCase.newInvitations
        .asLiveData()
        .cachedIn(viewModelScope)

    var answeredInvitations = invitationListUseCase.answeredInvitations
        .asLiveData()
        .cachedIn(viewModelScope)

    var sentInvitations = invitationListUseCase.sentInvitations
        .asLiveData()
        .cachedIn(viewModelScope)

    fun answerToInvitation(answer: InvitationAnswer, invitationId: Int?) {
        doAsync {
            invitationListUseCase.answerToInvitation(invitationId, answer)
            _answerLiveData.postValue(true)
        }
    }
}