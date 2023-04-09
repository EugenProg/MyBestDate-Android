package com.bestDate.presentation.main.userProfile.invitationList

import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.model.ShortUserData
import com.bestDate.presentation.main.userProfile.invitationList.adapters.AnsweredInvitationsAdapter
import com.bestDate.presentation.main.userProfile.invitationList.adapters.BaseInvitationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnsweredInvitationFragment(override var navigateAction: (ShortUserData?) -> Unit) :
    BaseInvitationFragment(navigateAction) {

    override fun getInvitationAdapter(): BaseInvitationAdapter {
        return AnsweredInvitationsAdapter()
    }

    override fun getNoDataTitle(): Int = R.string.you_have_no_answered_invitations

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.answeredInvitations) {
            loadItems(it)
        }
    }
}