package com.bestDate.presentation.main.top

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentTopBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopFragment : BaseVMFragment<FragmentTopBinding, TopViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTopBinding =
        { inflater, parent, attach -> FragmentTopBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<TopViewModel> = TopViewModel::class.java

    override val navBarColor = R.color.bg_main
    override val statusBarColor = R.color.bg_main

    override val statusBarLight = false
    override val navBarLight = false

    override fun onInit() {
        super.onInit()
        setUpToolbar()
        setUpFilterButtons()
        setUpResult()
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.top_50)
        binding.toolbar.onProfileClick = {
            findNavController().navigate(R.id.action_global_profile_nav_graph_from_top)
        }
    }

    private fun setUpFilterButtons() {
        binding.locationFilterButton.label = getString(R.string.universe)
        binding.locationFilterButton.isActive = true
        binding.manSelector.label = getString(R.string.man)
        binding.womanSelector.label = getString(R.string.woman)

    }

    fun setUpResult() {
        binding.resultView.percent = 80.0
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.user.observe(viewLifecycleOwner) {
            binding.toolbar.photo = it?.getMainPhotoThumbUrl()
            binding.manSelector.isActive = it?.look_for?.first() == getString(R.string.male)
            binding.womanSelector.isActive = it?.look_for?.first() == getString(R.string.female)
        }
    }
}