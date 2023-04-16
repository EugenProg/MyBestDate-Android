package com.bestDate.presentation.main.userProfile.matchesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.FragmentMatchesListBinding
import com.bestDate.presentation.base.BaseVMFragment
import com.bestDate.view.alerts.showMatchActionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class MatchesListFragment :
    BaseVMFragment<FragmentMatchesListBinding, MatchesListViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMatchesListBinding =
        { inflater, parent, attach -> FragmentMatchesListBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<MatchesListViewModel> = MatchesListViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private lateinit var adapter: MatchesListAdapter
    private var refreshingMode: Boolean = false

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.toolbar.backClick = {
            goBack()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.userPhoto) {
            initListData(it)
        }
        observe(viewModel.errorLiveData) {
            binding.refreshView.isRefreshing = false
            binding.noDataView.toggleLoading(false)
            showMessage(it.exception.message)
        }
    }

    private fun initListData(myPhotoUrl: String?) {
        adapter = MatchesListAdapter(myPhotoUrl.orEmpty())
        binding.matchesListView.layoutManager = LinearLayoutManager(requireContext())
        binding.matchesListView.adapter = adapter

        adapter.itemClick = { item, type ->
            if (type == MatchesSelectType.USER) {
                navigateToUserProfile(item.user)
            } else {
                requireActivity().showMatchActionDialog(item, myPhotoUrl.orEmpty(), {
                    navigateToUserProfile(it)
                }, {
                    navigateToChat(it)
                })
            }
        }
        initListObserver()
    }

    private fun initListObserver() {
        observe(viewModel.matchesList) {
            adapter.submitData(lifecycle, it)
            viewModel.areMatchesViewed()
        }

        with(binding) {
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

    open fun navigateToUserProfile(userData: ShortUserData?) {
        navController.navigate(
            MatchesListFragmentDirections
                .actionGlobalAnotherProfile(userData, BackScreenType.PROFILE)
        )
    }

    open fun navigateToChat(userData: ShortUserData?) {
        navController.navigate(
            MatchesListFragmentDirections
                .actionGlobalProfileToChat(userData, BackScreenType.PROFILE)
        )
    }
}