package com.bestDate.presentation.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bestDate.R
import com.bestDate.databinding.FragmentOnboardSecondBinding
import com.bestDate.base.BaseFragment

class OnboardSecondFragment : BaseFragment<FragmentOnboardSecondBinding>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboardSecondBinding =
        { inflater, parent, attach -> FragmentOnboardSecondBinding.inflate(inflater, parent, attach) }

    override val navBarColor: Int = R.color.bg_light_blue
    override val navBarLight = true
    override val statusBarColor: Int = R.color.bg_main

    override fun onViewClickListener() {
        super.onViewClickListener()
        binding.startButton.setOnClickListener {
            navController.popBackStack()
        }
        binding.authButton.setOnClickListener {
            navController.navigate(OnboardSecondFragmentDirections
                .actionOnboardSecondFragmentToAuthFragment())
        }
        binding.nextButton.setOnClickListener {
            navController.navigate(OnboardSecondFragmentDirections
                .actionOnboardSecondFragmentToAuthFragment())
        }
        binding.skipButton.setOnClickListener {
            navController.navigate(OnboardSecondFragmentDirections
                .actionOnboardSecondFragmentToAuthFragment())
        }
    }
}