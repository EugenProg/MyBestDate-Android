package com.bestDate.presentation.base.anotherProfile

import androidx.lifecycle.MutableLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.db.entity.UserDB
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnotherProfileViewModel @Inject constructor(
    private val anotherProfileUseCase: AnotherProfileUseCase
): BaseViewModel() {

    var user: MutableLiveData<UserDB> = anotherProfileUseCase.user

    private var _blockLiveData: LiveEvent<Boolean> = LiveEvent()
    var blockLiveData: LiveEvent<Boolean> = _blockLiveData

    fun getUserById(id: Int?) {
        doAsync {
            anotherProfileUseCase.getUserById(id)
        }
    }

    fun clearUserData() {
        anotherProfileUseCase.user.postValue(null)
    }

    fun blockUser(id: Int?) {
        doAsync {
            anotherProfileUseCase.blockUser(id)
            anotherProfileUseCase.getUserById(id)
            blockLiveData.postValue(true)
        }
    }

    fun unlockUser(id: Int?) {
        doAsync {
            anotherProfileUseCase.unlockUser(id)
            anotherProfileUseCase.getUserById(id)
            blockLiveData.postValue(false)
        }
    }
}