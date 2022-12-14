package com.bestDate.network.remote

import com.bestDate.data.extension.orZero
import com.bestDate.data.model.InvitationAnswer
import com.bestDate.data.model.InvitationAnswerRequest
import com.bestDate.data.model.SendInvitationRequest
import com.bestDate.network.services.InvitationService
import javax.inject.Inject

class InvitationsRemoteData @Inject constructor(
    private val service: InvitationService
) {

    suspend fun getInvitationList() = service.getInvitations()

    suspend fun sendInvitation(userId: Int, invitationId: Int) =
        service.sendInvitation(SendInvitationRequest(invitationId, userId))

    suspend fun answerToInvitation(invitationId: Int?, answer: InvitationAnswer) =
        service.answerToInvitation(invitationId.orZero, InvitationAnswerRequest(answer.id))
}