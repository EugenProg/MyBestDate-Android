package com.bestDate.presentation.main.userProfile.invitationList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.FragmentInvitationBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.presentation.main.userProfile.invitationList.adapters.SentInvitationsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SentInvitationFragment(var navigateAction: (ShortUserData?) -> Unit) :
    BaseVMFragment<FragmentInvitationBinding, InvitationListViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInvitationBinding =
        { inflater, parent, attach -> FragmentInvitationBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<InvitationListViewModel> =
        InvitationListViewModel::class.java
    override val statusBarColor = R.color.bg_main

    private var adapter: SentInvitationsAdapter = SentInvitationsAdapter()

    override fun onInit() {
        super.onInit()
        with(binding) {
            noDataView.setTitle(getString(R.string.you_have_no_invitations_sent))

            invitationListView.layoutManager = LinearLayoutManager(requireContext())
            invitationListView.adapter = adapter

            refreshView.setOnRefreshListener {
                viewModel.refreshSentInvitationList()
            }
        }

        viewModel.refreshSentInvitationList()
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        adapter.userClick = {
            navigateAction.invoke(it)
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.sentInvitations) {
            adapter.submitList(it) {
                binding.refreshView.isRefreshing = false
                binding.noDataView.noData = it.isEmpty()
            }
        }
        observe(viewModel.loadingMode) {
            if (!binding.refreshView.isRefreshing &&
                viewModel.sentInvitations.value.isNullOrEmpty()
            ) binding.noDataView.toggleLoading(it)
        }
        observe(viewModel.errorLiveData) {
            binding.refreshView.isRefreshing = false
            binding.noDataView.toggleLoading(false)
            showMessage(it.exception.message)
        }
    }
}