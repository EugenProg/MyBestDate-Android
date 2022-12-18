package com.bestDate.presentation.main.userProfile.invitationList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentInvitationBinding
import com.bestDate.presentation.main.userProfile.invitationList.adapters.AnsweredInvitationsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnsweredInvitationFragment :
    BaseVMFragment<FragmentInvitationBinding, InvitationListViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInvitationBinding =
        { inflater, parent, attach -> FragmentInvitationBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<InvitationListViewModel> =
        InvitationListViewModel::class.java
    override val statusBarColor = R.color.bg_main

    private var adapter: AnsweredInvitationsAdapter = AnsweredInvitationsAdapter()

    override fun onInit() {
        super.onInit()
        with(binding) {
            noDataView.setTitle(getString(R.string.you_have_no_answered_invitations))

            invitationListView.layoutManager = LinearLayoutManager(requireContext())
            invitationListView.adapter = adapter

            refreshView.setOnRefreshListener {
                viewModel.refreshAnsweredInvitationList()
            }
        }

        viewModel.refreshAnsweredInvitationList()
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        adapter.userClick = {
            navController.navigate(InvitationListFragmentDirections.actionGlobalAnotherProfile(it))
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.answeredInvitations.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.refreshView.isRefreshing = false
                binding.noDataView.noData = it.isEmpty()
            }
        }
        viewModel.loadingMode.observe(viewLifecycleOwner) {
            if (!binding.refreshView.isRefreshing &&
                viewModel.answeredInvitations.value.isNullOrEmpty()
            ) binding.noDataView.toggleLoading(it)
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            binding.refreshView.isRefreshing = false
            binding.noDataView.toggleLoading(false)
            showMessage(it.exception.message)
        }
    }
}