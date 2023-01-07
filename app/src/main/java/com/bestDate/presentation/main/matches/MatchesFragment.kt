package com.bestDate.presentation.main.matches

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.data.extension.observe
import com.bestDate.databinding.FragmentMatchesBinding
import com.bestDate.presentation.base.BaseVMFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchesFragment : BaseVMFragment<FragmentMatchesBinding, MatchesViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMatchesBinding =
        { inflater, parent, attach -> FragmentMatchesBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<MatchesViewModel> = MatchesViewModel::class.java

    override val navBarColor = R.color.bg_main
    override val statusBarColor = R.color.bg_main

    override val statusBarLight = false
    override val navBarLight = false


    override fun onInit() {
        super.onInit()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.matches)
        binding.toolbar.onProfileClick = {
            navController.navigate(R.id.action_global_profile_nav_graph_from_matches)
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        observe(viewModel.user) {
            binding.toolbar.photo = it?.getMainPhotoThumbUrl()
        }
    }
}