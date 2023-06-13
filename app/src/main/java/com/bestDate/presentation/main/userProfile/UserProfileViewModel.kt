package com.bestDate.presentation.main.userProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.data.utils.subscription.SubscriptionUtil
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.main.userProfile.invitationList.InvitationListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val subscriptionUtil: SubscriptionUtil,
    invitationListUseCase: InvitationListUseCase
): BaseViewModel() {

    var user = userUseCase.getMyUser.asLiveData()
    var coins = userUseCase.coinsCount
    var hasNewLikes = userUseCase.hasNewLikes
    var hasNewMatches = userUseCase.hasNewMatches
    var hasNewDuels = userUseCase.hasNewDuels
    var hasNewInvitations = invitationListUseCase.hasNewInvitations

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

    fun getStartWithPhotoSelector() = userUseCase.startWithPhotoSelect
    fun needUpdate() = userUseCase.userNeedRefresh

    fun setStartWithPhotoSelector(enable: Boolean) {
        userUseCase.startWithPhotoSelect = enable
    }

    fun getSubscriptionEndDate() = subscriptionUtil.getSubscriptionEndDate()
}