package com.bestDate.presentation.main.userProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): BaseViewModel() {

    var user = userUseCase.getMyUser.asLiveData()
    var coins = userUseCase.coinsCount

    private var _signOutLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var signOutLiveData: LiveData<Boolean> = _signOutLiveData

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