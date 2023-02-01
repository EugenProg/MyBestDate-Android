package com.bestDate.presentation.main.duels.top

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bestDate.R
import com.bestDate.data.extension.observe
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
            root.layoutManager = GridLayoutManager(requireContext(), 2)
            root.adapter = adapter
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.topsMan) {
            adapter.items = it
        }
    }
}