package com.bestDate.presentation.main.anotherProfile

import androidx.navigation.fragment.navArgs
import com.bestDate.data.extension.orZero
import com.bestDate.data.model.BackScreenType
import com.bestDate.presentation.base.anotherProfile.AnotherProfileSliderFragment

class MainAnotherProfileSliderFragment: AnotherProfileSliderFragment() {

    private val args by navArgs<MainAnotherProfileSliderFragmentArgs>()

    override fun getPosition() = args.position

    override fun getUserId() = args.user.id.orZero

    override fun navigateToTariffList() {
        navController.navigate(
            MainAnotherProfileFragmentDirections.actionGlobalAnotherProfileToTariffList()
        )
    }

    override fun navigateToChat() {
        navController.navigate(
            MainAnotherProfileFragmentDirections
                .actionGlobalAnotherProfileToChat(args.user, BackScreenType.ANOTHER_PROFILE)
        )
    }
}