package com.bestDate.presentation.main.duels.top

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.data.model.BackScreenType
import com.bestDate.databinding.FragmentTopBinding
import com.bestDate.presentation.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopManFragment : BaseVMFragment<FragmentTopBinding, TopViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTopBinding =
        { inflater, parent, attach -> FragmentTopBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<TopViewModel> = TopViewModel::class.java

    override val statusBarColor = R.color.bg_main
    private val adapter = TopAdapter()

    override fun onInit() {
        super.onInit()
        with(binding) {
            listView.layoutManager = GridLayoutManager(requireContext(), 2)
            listView.adapter = adapter
        }

        adapter.itemClick = {
            navController.navigate(
                TopListFragmentDirections
                    .actionGlobalTopToAnotherProfile(it?.user, BackScreenType.DUELS)
            )
        }
        adapter.addLoadStateListener {
            binding.progress.isVisible = (it.source.refresh is LoadState.Loading) == true
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.topsMan) {
            adapter.submitData(lifecycle, it)
        }
    }
}