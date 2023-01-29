package com.bestDate.presentation.main.matches

import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.ShortUserData
import com.bestDate.presentation.main.userProfile.matchesList.MatchesListFragment

class MatchesMatchesListFragment: MatchesListFragment() {
    override fun navigateToChat(userData: ShortUserData?) {
        navController.navigate(
            MatchesMatchesListFragmentDirections
                .actionGlobalMatchesToChat(userData, BackScreenType.PROFILE)
        )
    }

    override fun navigateToUserProfile(userData: ShortUserData?) {
        navController.navigate(
            MatchesMatchesListFragmentDirections
                .actionGlobalMatchesToAnotherProfile(userData, BackScreenType.PROFILE)
        )
    }

    override var customBackNavigation: Boolean = true
}