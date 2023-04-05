package com.bestDate.presentation.main.userProfile.myDuels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.ShortUserData
import com.bestDate.databinding.FragmentMyDuelsBinding
import com.bestDate.presentation.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class MyDuelsFragment : BaseVMFragment<FragmentMyDuelsBinding, MyDuelsViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMyDuelsBinding =
        { inflater, parent, attach -> FragmentMyDuelsBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<MyDuelsViewModel> = MyDuelsViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private lateinit var adapter: MyDuelsAdapter

    override fun onInit() {
        super.onInit()

        adapter = MyDuelsAdapter()
        binding.myDuelsView.layoutManager = LinearLayoutManager(requireContext())
        binding.myDuelsView.adapter = adapter

        adapter.itemClick = {
            navigateToUserProfile(it)
        }

        binding.refreshView.setOnRefreshListener {
            adapter.refresh()
        }

        adapter.addLoadStateListener {
            binding.refreshView.isRefreshing = it.source.refresh is LoadState.Loading
            binding.noDataView.noData = it.source.refresh !is LoadState.Loading && adapter.itemCount == 0
            binding.noDataView.toggleLoading(it.source.refresh is LoadState.Loading && !binding.refreshView.isRefreshing)
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

        observe(viewModel.myDuels) {
            adapter.submitData(lifecycle, it)
        }
        observe(viewModel.errorLiveData) {
            binding.refreshView.isRefreshing = false
            binding.noDataView.toggleLoading(false)
            showMessage(it.exception.message)
        }
    }

    open fun navigateToUserProfile(userData: ShortUserData?) {
        navController.navigate(
            MyDuelsFragmentDirections
                .actionGlobalAnotherProfile(userData, BackScreenType.PROFILE)
        )
    }
}