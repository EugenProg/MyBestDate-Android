package com.bestDate.presentation.main.userProfile.invitationList

import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.model.ShortUserData
import com.bestDate.presentation.main.userProfile.invitationList.adapters.BaseInvitationAdapter
import com.bestDate.presentation.main.userProfile.invitationList.adapters.NewInvitationsAdapter
import com.bestDate.view.alerts.LoaderDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewInvitationFragment(override var navigateAction: (ShortUserData?) -> Unit) :
    BaseInvitationFragment(navigateAction) {
    private lateinit var loaderDialog: LoaderDialog

    override fun getInvitationAdapter(): BaseInvitationAdapter {
        return NewInvitationsAdapter()
    }

    override fun getNoDataTitle(): Int = R.string.you_have_no_new_invitations

    override fun onInit() {
        super.onInit()
        loaderDialog = LoaderDialog(requireActivity())
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        adapter.answerClick = { answer, id ->
            loaderDialog.startLoading()
            viewModel.answerToInvitation(answer, id)
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.newInvitations) {
            loadItems(it)
        }
        observe(viewModel.answerLiveData) {
            if (it) {
                loaderDialog.stopLoading()
                adapter.refresh()
            }
        }
    }
}