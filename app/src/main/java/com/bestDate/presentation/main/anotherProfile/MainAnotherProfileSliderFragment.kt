package com.bestDate.presentation.main.anotherProfile

import androidx.navigation.fragment.navArgs
import com.bestDate.presentation.base.anotherProfile.AnotherProfileSliderFragment

class MainAnotherProfileSliderFragment: AnotherProfileSliderFragment() {

    private val args by navArgs<MainAnotherProfileSliderFragmentArgs>()

    override fun getPosition() = args.position

    override fun getUserId() = args.userId

    override fun navigateToTariffList() {
        navController.navigate(
            MainAnotherProfileFragmentDirections.actionGlobalAnotherProfileToTariffList()
        )
    }
}