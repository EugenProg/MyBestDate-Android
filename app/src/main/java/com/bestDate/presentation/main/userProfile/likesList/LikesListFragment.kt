package com.bestDate.presentation.main.userProfile.likesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentLikesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikesListFragment: BaseVMFragment<FragmentLikesListBinding, LikesListViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLikesListBinding = {
        inflater, parent, attach -> FragmentLikesListBinding.inflate(inflater, parent, attach)
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

        }

        binding.refreshView.setOnRefreshListener {
            viewModel.getLikes()
        }

        viewModel.getLikes()
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()

        viewModel.likesList.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.refreshView.isRefreshing = false
            }
        }
        viewModel.errorLive.observe(viewLifecycleOwner) {
            showMessage(it.exception.message)
        }
    }
}