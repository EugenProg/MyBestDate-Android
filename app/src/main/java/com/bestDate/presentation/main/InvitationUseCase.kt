package com.bestDate.presentation.main

import com.bestDate.data.extension.orZero
import com.bestDate.data.model.InternalException
import com.bestDate.db.dao.InvitationDao
import com.bestDate.network.remote.InvitationsRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvitationUseCase @Inject constructor(
    private var remoteData: InvitationsRemoteData,
    private var invitationDao: InvitationDao
) {

    var invitations = invitationDao.getInvitations()

    suspend fun refreshInvitations() {
        val response = remoteData.getInvitationList()
        if (response.isSuccessful) {
            response.body()?.let {
                invitationDao.save(it.data)
            }
        }
    }

    suspend fun sendInvitation(userId: Int?, invitationId: Int) {
        val response = remoteData.sendInvitation(userId.orZero, invitationId)
        if (!response.isSuccessful) throw InternalException.OperationException(response.message())
    }
}