package com.bestDate.presentation.main.userProfile.settings.bockedUsers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.databinding.FragmentBlockedUsersBinding
import com.bestDate.view.alerts.LoaderDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlockedUsersFragment : BaseVMFragment<FragmentBlockedUsersBinding, BlockedUsersViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBlockedUsersBinding =
        { inflater, parent, attach ->
            FragmentBlockedUsersBinding.inflate(
                inflater,
                parent,
                attach
            )
        }
    override val viewModelClass: Class<BlockedUsersViewModel> = BlockedUsersViewModel::class.java

    private lateinit var adapter: BlockedUserAdapter
    override val statusBarColor = R.color.bg_main

    private lateinit var loader: LoaderDialog

    override fun onInit() {
        super.onInit()
        loader = LoaderDialog(requireActivity())
        adapter = BlockedUserAdapter()
        with(binding) {
            blockedUsersList.layoutManager = LinearLayoutManager(requireContext())
            blockedUsersList.adapter = adapter
        }

        viewModel.refreshBlockedUserList()
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            toolbar.backClick = {
                goBack()
            }

            adapter.openClick = {
                navController.navigate(BlockedUsersFragmentDirections.actionGlobalAnotherProfile(it))
            }
            adapter.unlockClick = {
                loader.startLoading()
                viewModel.unlockUser(it)
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.blockedUsersList.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.noDataView.noData = it.isEmpty()
            }
        }
        viewModel.unlockSuccessfulLiveData.observe(viewLifecycleOwner) {
            loader.stopLoading()
        }
        viewModel.loadingMode.observe(viewLifecycleOwner) {
            if (viewModel.blockedUsersList.value.isNullOrEmpty()) binding.noDataView.toggleLoading(
                it
            )
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            showMessage(it.exception.message)
            loader.stopLoading()
            binding.noDataView.toggleLoading(false)
        }
    }
}