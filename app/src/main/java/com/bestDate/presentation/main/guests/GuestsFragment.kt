package com.bestDate.presentation.main.guests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.databinding.FragmentGuestsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuestsFragment : BaseVMFragment<FragmentGuestsBinding, GuestsViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGuestsBinding =
        { inflater, parent, attach -> FragmentGuestsBinding.inflate(inflater, parent, attach) }
    override val viewModelClass: Class<GuestsViewModel> = GuestsViewModel::class.java

    override val navBarColor = R.color.bg_main
    override val statusBarColor = R.color.bg_main

    override val statusBarLight = false
    override val navBarLight = false


    override fun onInit() {
        super.onInit()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        binding.toolbar.title = getString(R.string.guests)
        binding.toolbar.onProfileClick = {
            findNavController().navigate(R.id.action_global_profile_nav_graph_from_guests)
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.user.observe(viewLifecycleOwner) {
            binding.toolbar.photo = it?.getMainPhotoThumbUrl()
        }
    }
}