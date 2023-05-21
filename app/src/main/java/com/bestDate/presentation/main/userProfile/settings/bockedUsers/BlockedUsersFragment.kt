package com.bestDate.presentation.main.userProfile.settings.bockedUsers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.model.BackScreenType
import com.bestDate.databinding.FragmentBlockedUsersBinding
import com.bestDate.presentation.base.BaseVMFragment
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
                navController.navigate(
                    BlockedUsersFragmentDirections
                        .actionGlobalSettingsToAnotherProfile(it, BackScreenType.PROFILE)
                )
            }
            adapter.unlockClick = {
                loader.startLoading()
                viewModel.unlockUser(it)
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.blockedUsersList) {
            adapter.submitList(it) {
                binding.noDataView.noData = it.isEmpty()
            }
        }
        observe(viewModel.unlockSuccessfulLiveData) {
            loader.stopLoading()
        }
        observe(viewModel.loadingMode) {
            if (viewModel.blockedUsersList.value.isNullOrEmpty()) binding.noDataView.toggleLoading(
                it
            )
        }
        observe(viewModel.errorLiveData) {
            showMessage(it.exception.message)
            loader.stopLoading()
            binding.noDataView.toggleLoading(false)
        }
    }
}