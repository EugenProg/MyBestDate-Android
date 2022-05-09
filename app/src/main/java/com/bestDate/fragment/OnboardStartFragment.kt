package com.bestDate.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.databinding.FragmentOnboardStartBinding

class OnboardStartFragment : BaseFragment<FragmentOnboardStartBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboardStartBinding =
        { inflater, parent, attach -> FragmentOnboardStartBinding.inflate(inflater, parent, attach) }

    override val navBarColor = R.color.bg_pink
    override val statusBarColor = R.color.bg_main

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.nextButton.setOnClickListener {
            navController.navigate(OnboardStartFragmentDirections
                .actionOnboardStartFragmentToOnboardSecondFragment())
        }
        binding.skipButton.setOnClickListener { //TODO change to auth screen
            navController.navigate(OnboardStartFragmentDirections
                .actionOnboardStartFragmentToAuthFragment())
        }
        binding.secondButton.setOnClickListener {
            navController.navigate(OnboardStartFragmentDirections
                .actionOnboardStartFragmentToOnboardSecondFragment())
        }
        binding.authButton.setOnClickListener { //TODO change to auth screen
            navController.navigate(OnboardStartFragmentDirections
                .actionOnboardStartFragmentToAuthFragment())
        }
    }
}