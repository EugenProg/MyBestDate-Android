package com.bestDate.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.base.BaseFragment
import com.bestDate.databinding.FragmentStartBinding

class StartFragment : BaseFragment<FragmentStartBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStartBinding =
        { inflater, parent, attach -> FragmentStartBinding.inflate(inflater, parent, attach) }

    override fun onInit() {
        super.onInit()
        navController.navigate(StartFragmentDirections.actionStartFragmentToOnboardStartFragment())
    }
}