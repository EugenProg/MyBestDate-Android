package com.bestDate.presentation.main.userProfile.personalData.changePassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase
) : BaseViewModel() {

    private var _successLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var successLiveData: LiveData<Boolean> = _successLiveData

    fun changePassword(oldPass: String, newPass: String) {
        doAsync {
            changePasswordUseCase.changePassword(oldPass, newPass)
            _successLiveData.postValue(true)
        }
    }
}