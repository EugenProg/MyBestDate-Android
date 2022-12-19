package com.bestDate.presentation.main.userProfile.settings.bockedUsers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.data.model.ShortUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BlockedUsersViewModel @Inject constructor(
    private val blockedUserUseCase: BlockedUserUseCase
) : BaseViewModel() {

    var blockedUsersList: LiveData<MutableList<ShortUserData>> = blockedUserUseCase.blockedUsers

    private var _unlockSuccessfulLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var unlockSuccessfulLiveData: LiveData<Boolean> = _unlockSuccessfulLiveData

    fun refreshBlockedUserList() {
        doAsync {
            blockedUserUseCase.refreshBlockedUsersList()
        }
    }

    fun unlockUser(id: Int?) {
        doAsync {
            blockedUserUseCase.unlockUser(id)
            blockedUserUseCase.refreshBlockedUsersList()
            _unlockSuccessfulLiveData.postValue(true)
        }
    }
}