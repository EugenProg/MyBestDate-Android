package com.bestDate.presentation.main.userProfile.invitationList

import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.model.ShortUserData
import com.bestDate.presentation.main.userProfile.invitationList.adapters.BaseInvitationAdapter
import com.bestDate.presentation.main.userProfile.invitationList.adapters.SentInvitationsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SentInvitationFragment(override var navigateAction: (ShortUserData?) -> Unit) :
    BaseInvitationFragment(navigateAction) {
    override fun getInvitationAdapter(): BaseInvitationAdapter {
        return SentInvitationsAdapter()
    }

    override fun getNoDataTitle(): Int = R.string.you_have_no_invitations_sent

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.sentInvitations) {
            loadItems(it)
        }
    }
}