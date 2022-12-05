package com.bestDate.presentation.main.userProfile.myDuels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentMyDuelsBinding
import com.bestDate.presentation.main.userProfile.likesList.LikesListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDuelsFragment: BaseVMFragment<FragmentMyDuelsBinding, MyDuelsViewModel>() {
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

        }

        binding.refreshView.setOnRefreshListener {
            viewModel.getMyDuels()
        }

        viewModel.getMyDuels()
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.toolbar.backClick = {
            goBack()
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()

        viewModel.myDuels.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.refreshView.isRefreshing = false
                binding.noDataView.noData = it.isEmpty()
            }
        }
        viewModel.loadingMode.observe(viewLifecycleOwner) {
            if (!binding.refreshView.isRefreshing &&
                viewModel.myDuels.value.isNullOrEmpty()) binding.noDataView.toggleLoading(it)
        }
        viewModel.errorLive.observe(viewLifecycleOwner) {
            binding.refreshView.isRefreshing = false
            binding.noDataView.toggleLoading(false)
            showMessage(it.exception.message)
        }
    }
}