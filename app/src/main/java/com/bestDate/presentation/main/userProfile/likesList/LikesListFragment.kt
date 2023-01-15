package com.bestDate.presentation.main.userProfile.likesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.model.BackScreenType
import com.bestDate.databinding.FragmentLikesListBinding
import com.bestDate.presentation.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikesListFragment : BaseVMFragment<FragmentLikesListBinding, LikesListViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLikesListBinding =
        { inflater, parent, attach ->
            FragmentLikesListBinding.inflate(inflater, parent, attach)
        }
    override val viewModelClass: Class<LikesListViewModel> = LikesListViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private lateinit var adapter: LikesListAdapter

    override fun onInit() {
        super.onInit()

        adapter = LikesListAdapter()
        binding.likesListView.layoutManager = LinearLayoutManager(requireContext())
        binding.likesListView.adapter = adapter

        adapter.itemClick = {
            navController.navigate(
                LikesListFragmentDirections
                    .actionGlobalAnotherProfile(it.user, BackScreenType.PROFILE)
            )
        }

        binding.refreshView.setOnRefreshListener {
            viewModel.getLikes()
        }

        viewModel.getLikes()
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.toolbar.backClick = {
            goBack()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()

        observe(viewModel.likesList) {
            adapter.submitList(it) {
                binding.refreshView.isRefreshing = false
                binding.noDataView.noData = it.isEmpty()
            }
        }
        observe(viewModel.loadingMode) {
            if (!binding.refreshView.isRefreshing &&
                viewModel.likesList.value.isNullOrEmpty()
            ) binding.noDataView.toggleLoading(it)
        }
        observe(viewModel.errorLiveData) {
            binding.refreshView.isRefreshing = false
            binding.noDataView.toggleLoading(false)
            showMessage(it.exception.message)
        }
    }
}