package com.bestDate.presentation.registrationOAuthData

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.data.model.UpdateUserRequest
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.userProfile.personalData.PersonalDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FillRegistrationOAuthDataViewModel @Inject constructor(
    private val personalDataUseCase: PersonalDataUseCase
) : BaseViewModel() {

    private var _saveSuccessLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var saveSuccessLiveData: LiveData<Boolean> = _saveSuccessLiveData

    fun saveUserData(updateUserRequest: UpdateUserRequest) {
        doAsync {
            personalDataUseCase.updateUserInfo(updateUserRequest)
            _saveSuccessLiveData.postValue(true)
        }
    }
}