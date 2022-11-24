package com.bestDate.presentation.main.userProfile

import androidx.lifecycle.asLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): BaseViewModel() {

    var user = userUseCase.getMyUser.asLiveData()

    fun updateUserData() {
        doAsync {
            userUseCase.refreshUser()
        }
    }

    fun signOut() {
        doAsync {
            userUseCase.logout()
        }
    }
}