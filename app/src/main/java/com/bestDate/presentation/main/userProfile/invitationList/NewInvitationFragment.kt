package com.bestDate.presentation.main.userProfile.invitationList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentInvitationBinding
import com.bestDate.presentation.main.userProfile.invitationList.adapters.NewInvitationsAdapter
import com.bestDate.view.alerts.LoaderDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewInvitationFragment: BaseVMFragment<FragmentInvitationBinding, InvitationListViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInvitationBinding =
        { inflater, parent, attach -> FragmentInvitationBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<InvitationListViewModel> = InvitationListViewModel::class.java
    override val statusBarColor = R.color.bg_main

    private var adapter: NewInvitationsAdapter = NewInvitationsAdapter()
    private lateinit var loaderDialog: LoaderDialog

    override fun onInit() {
        super.onInit()
        loaderDialog = LoaderDialog(requireActivity())
        with(binding) {
            noDataView.setTitle(getString(R.string.you_have_no_new_invitations))

            invitationListView.layoutManager = LinearLayoutManager(requireContext())
            invitationListView.adapter = adapter

            refreshView.setOnRefreshListener {
                viewModel.refreshNewInvitationList()
            }
        }

        viewModel.refreshNewInvitationList()
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        adapter.userClick = {
            navController.navigate(InvitationListFragmentDirections.actionGlobalAnotherProfile(it))
        }

        adapter.answerClick = { answer, id ->
            loaderDialog.startLoading()
            viewModel.answerToInvitation(answer, id)
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.newInvitations.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.refreshView.isRefreshing = false
                binding.noDataView.noData = it.isEmpty()
            }
        }
        viewModel.loadingMode.observe(viewLifecycleOwner) {
            if (!it) loaderDialog.stopLoading()
            if (!binding.refreshView.isRefreshing &&
                viewModel.newInvitations.value.isNullOrEmpty()) binding.noDataView.toggleLoading(it)
        }
        viewModel.errorLive.observe(viewLifecycleOwner) {
            binding.refreshView.isRefreshing = false
            binding.noDataView.toggleLoading(false)
            showMessage(it.exception.message)
        }
    }
}