package com.bestDate.presentation.main.anotherProfile

import androidx.navigation.fragment.navArgs
import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.ShortUserData
import com.bestDate.presentation.base.anotherProfile.BaseAnotherProfileFragment

class MainAnotherProfileFragment: BaseAnotherProfileFragment() {

    private val args by navArgs<MainAnotherProfileFragmentArgs>()

    override fun onInit() {
        user = args.user
        viewModel.getUserById(user?.id)
        super.onInit()
    }

    override fun getBackScreen() = args.backScreen

    override fun navigateToChat() {
        navController.navigate(
            MainAnotherProfileFragmentDirections
                .actionGlobalAnotherProfileToChat(user, BackScreenType.ANOTHER_PROFILE)
        )
    }

    override fun navigateToSlider(position: Int) {
        navController.navigate(
            MainAnotherProfileFragmentDirections
                .actionAnotherProfileToSlider(
                    fullUser?.getShortUser(null) ?: ShortUserData(), position)
        )
    }

    override fun navigateToQuestionnaire() {
        navController.navigate(
            MainAnotherProfileFragmentDirections.actionAnotherProfileToQuestionnaire()
        )
    }

    override fun navigateToSearch() {
        navController.navigate(
            MainAnotherProfileFragmentDirections.actionGlobalAnotherProfileToSearch()
        )
    }

    override fun navigateToTariffList() {
        navController.navigate(
            MainAnotherProfileFragmentDirections.actionGlobalAnotherProfileToTariffList()
        )
    }
}