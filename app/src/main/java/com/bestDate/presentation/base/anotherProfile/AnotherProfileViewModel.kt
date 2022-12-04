package com.bestDate.presentation.base.anotherProfile

import androidx.lifecycle.MutableLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.db.entity.UserDB
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnotherProfileViewModel @Inject constructor(
    private val anotherProfileUseCase: AnotherProfileUseCase
): BaseViewModel() {

    var user: MutableLiveData<UserDB> = anotherProfileUseCase.user

    fun getUserById(id: Int?) {
        doAsync {
            anotherProfileUseCase.getUserById(id)
        }
    }

    fun clearUserData() {
        anotherProfileUseCase.user.postValue(null)
    }
}