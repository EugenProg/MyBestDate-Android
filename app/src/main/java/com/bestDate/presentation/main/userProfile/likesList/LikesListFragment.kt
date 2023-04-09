package com.bestDate.presentation.main.userProfile.likesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.Like
import com.bestDate.databinding.FragmentLikesListBinding
import com.bestDate.presentation.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class LikesListFragment : BaseVMFragment<FragmentLikesListBinding, LikesListViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLikesListBinding =
        { inflater, parent, attach ->
            FragmentLikesListBinding.inflate(inflater, parent, attach)
        }
    override val viewModelClass: Class<LikesListViewModel> = LikesListViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private lateinit var adapter: LikesListAdapter
    private var refreshingMode: Boolean = false

    open fun navigateToUserProfile(like: Like) {
        navController.navigate(
            LikesListFragmentDirections
                .actionGlobalAnotherProfile(like.user, BackScreenType.PROFILE)
        )
    }

    override fun onInit() {
        super.onInit()

        adapter = LikesListAdapter()
        with(binding) {
            likesListView.layoutManager = LinearLayoutManager(requireContext())
            likesListView.adapter = adapter

            adapter.itemClick = {
                navigateToUserProfile(it)
            }

            refreshView.setOnRefreshListener {
                refreshingMode = true
                adapter.refresh()
            }

            adapter.addLoadStateListener {
                refreshView.isRefreshing = it.source.refresh is LoadState.Loading && refreshingMode
                noDataView.noData =
                    it.source.refresh !is LoadState.Loading && adapter.itemCount == 0
                noDataView.toggleLoading(it.source.refresh is LoadState.Loading && !refreshingMode)
            }
        }

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
            adapter.submitData(lifecycle, it)
        }
        observe(viewModel.errorLiveData) {
            binding.refreshView.isRefreshing = false
            binding.noDataView.toggleLoading(false)
            showMessage(it.exception.message)
        }
    }
}