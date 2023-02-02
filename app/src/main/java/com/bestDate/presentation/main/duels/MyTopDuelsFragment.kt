package com.bestDate.presentation.main.duels

import com.bestDate.data.model.BackScreenType
import com.bestDate.data.model.ShortUserData
import com.bestDate.presentation.main.userProfile.myDuels.MyDuelsFragment

class MyTopDuelsFragment: MyDuelsFragment() {

    override fun navigateToUserProfile(userData: ShortUserData?) {
        navController.navigate(
            MyTopDuelsFragmentDirections
                .actionGlobalTopToAnotherProfile(userData, BackScreenType.PROFILE)
        )
    }
}