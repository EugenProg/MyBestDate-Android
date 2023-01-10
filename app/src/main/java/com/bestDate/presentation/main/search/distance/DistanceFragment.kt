package com.bestDate.presentation.main.search.distance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bestDate.R
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.utils.CityListItem
import com.bestDate.databinding.FragmentDistanceBinding
import com.bestDate.presentation.base.BaseFragment
import com.bestDate.view.alerts.LoaderDialog

class DistanceFragment(var userLocation: String? = null) : BaseFragment<FragmentDistanceBinding>() {

    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDistanceBinding =
        { inflater, parent, attach -> FragmentDistanceBinding.inflate(inflater, parent, attach) }
    private lateinit var loader: LoaderDialog
    private var selectedLocation: CityListItem? = null

    override val statusBarLight = false
    override val navBarLight = false
    override val navBarColor = R.color.bg_main
    override val statusBarColor = R.color.bg_main

    var backClick: (() -> Unit)? = null
    var saveClick: ((CityListItem?, Int) -> Unit)? = null
    override var customBackNavigation = true

    override fun onInit() {
        super.onInit()
        binding.searchView.initSearching(
            lifecycleScope,
            viewLifecycleOwner,
            userLocation
        )
        binding.searchView.hidePoweredByGoogle()
        binding.searchView.onFocusChanged = {
            binding.seekBar.isVisible = !it
        }
        loader = LoaderDialog(requireActivity())

        binding.bar.run {
            minProgress = 0
            maxProgress = 300
            progress = getHalfDistance()
        }
    }

    private fun getHalfDistance(): Int {
        val max = binding.bar.maxProgress
        val min = binding.bar.minProgress
        return min + ((max - min) / 2)
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.backButton.onClick = {
            goBack()
        }
        binding.searchView.selectAction = {
            selectedLocation = it
        }
        binding.saveButton.setOnSaveClickListener {
            saveClick?.invoke(selectedLocation, binding.bar.progress)
        }
    }

    override fun goBack() {
        backClick?.invoke()
    }
}