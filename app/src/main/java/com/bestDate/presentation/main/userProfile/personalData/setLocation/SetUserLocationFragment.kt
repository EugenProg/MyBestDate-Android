package com.bestDate.presentation.main.userProfile.personalData.setLocation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.data.extension.setOnSaveClickListener
import com.bestDate.data.utils.CityListItem
import com.bestDate.databinding.FragmentSetUserLocationBinding
import com.bestDate.view.alerts.LoaderDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetUserLocationFragment :
    BaseVMFragment<FragmentSetUserLocationBinding, SetUserLocationViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSetUserLocationBinding =
        { inflater, parent, attach ->
            FragmentSetUserLocationBinding.inflate(
                inflater,
                parent,
                attach
            )
        }
    override val viewModelClass: Class<SetUserLocationViewModel> =
        SetUserLocationViewModel::class.java
    private val args by navArgs<SetUserLocationFragmentArgs>()

    override val statusBarColor = R.color.bg_main
    private var selectedLocation: CityListItem? = null
    private lateinit var loader: LoaderDialog

    override fun onInit() {
        super.onInit()
        binding.searchView.initSearching(lifecycleScope, viewLifecycleOwner, args.location)
        loader = LoaderDialog(requireActivity())
    }

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.searchView.selectAction = {
            selectedLocation = it
        }
        binding.backButton.onClick = {
            goBack()
        }
        binding.saveButton.setOnSaveClickListener {
            selectedLocation?.let {
                loader.startLoading()
                viewModel.saveUserLocation(it)
            }
        }
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.saveSuccessfulLivaData.observe(viewLifecycleOwner) {
            loader.stopLoading()
            showMessage(R.string.save_successfully)
            goBack()
        }
        viewModel.errorLive.observe(viewLifecycleOwner) {
            loader.stopLoading()
            showMessage(it.exception.message)
        }
    }
}