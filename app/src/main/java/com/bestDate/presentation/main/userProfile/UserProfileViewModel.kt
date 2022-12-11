package com.bestDate.presentation.main.userProfile

import androidx.lifecycle.asLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): BaseViewModel() {

    var user = userUseCase.getMyUser.asLiveData()

    private var _signOutLiveData: LiveEvent<Boolean> = LiveEvent()
    var signOutLiveData: LiveEvent<Boolean> = _signOutLiveData

    fun updateUserData() {
        doAsync {
            userUseCase.refreshUser()
        }
    }

    fun signOut() {
        doAsync {
            userUseCase.logout()
            _signOutLiveData.postValue(true)
        }
    }
}