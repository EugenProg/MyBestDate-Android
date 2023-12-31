package com.bestDate.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.presentation.base.BaseFragment
import com.bestDate.databinding.FragmentGeoEnableBinding

class GeoEnableFragment : BaseFragment<FragmentGeoEnableBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGeoEnableBinding =
        { inflater, parent, attach -> FragmentGeoEnableBinding.inflate(inflater, parent, attach) }

    override val navBarColor = R.color.white
    override val navBarLight = true
    override val statusBarLight = true

    override fun onViewClickListener() {
        super.onViewClickListener()
        with(binding) {
            skipButton.setOnClickListener {
                navController.navigate(GeoEnableFragmentDirections.actionGeoToMessagesSettings())
            }
            enableButton.onSafeClick = {
                navController.navigate(GeoEnableFragmentDirections.actionGeoToMessagesSettings())
            }
        }
    }
}