package com.bestDate.presentation.main.userProfile.likesList

import androidx.navigation.fragment.navArgs
import com.bestDate.presentation.base.anotherProfile.AnotherProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileToAnotherProfileFragment: AnotherProfileFragment() {
    private val args by navArgs<UserProfileToAnotherProfileFragmentArgs>()

    override fun getUserInfo() = args.user
}