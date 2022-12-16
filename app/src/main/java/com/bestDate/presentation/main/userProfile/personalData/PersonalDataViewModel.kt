package com.bestDate.presentation.main.userProfile.personalData

import androidx.lifecycle.asLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.data.model.UpdateUserRequest
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PersonalDataViewModel @Inject constructor(
    private var personalDataUseCase: PersonalDataUseCase
) : BaseViewModel() {

    var user = personalDataUseCase.getMyUser.asLiveData()

    private var _userSaveSuccessLiveData: LiveEvent<Boolean> = LiveEvent()
    var userSaveSuccessLiveData: LiveEvent<Boolean> = _userSaveSuccessLiveData

    private var _emailCodeSuccessLiveData: LiveEvent<Boolean> = LiveEvent()
    var emailCodeSuccessLiveData: LiveEvent<Boolean> = _emailCodeSuccessLiveData

    private var _emailSaveSuccessLiveData: LiveEvent<Boolean> = LiveEvent()
    var emailSaveSuccessLiveData: LiveEvent<Boolean> = _emailSaveSuccessLiveData

    private var _phoneCodeSuccessLiveData: LiveEvent<Boolean> = LiveEvent()
    var phoneCodeSuccessLiveData: LiveEvent<Boolean> = _phoneCodeSuccessLiveData

    private var _phoneSaveSuccessLiveData: LiveEvent<Boolean> = LiveEvent()
    var phoneSaveSuccessLiveData: LiveEvent<Boolean> = _phoneSaveSuccessLiveData

    fun updateUserInfo(userRequest: UpdateUserRequest) {
        doAsync {
            personalDataUseCase.updateUserInfo(userRequest)
            _userSaveSuccessLiveData.postValue(true)
        }
    }

    fun sendEmailCode(email: String) {
        doAsync {
            personalDataUseCase.sendEmailCode(email)
            _emailCodeSuccessLiveData.postValue(true)
        }
    }

    fun sendPhoneCode(phone: String) {
        doAsync {
            personalDataUseCase.sendPhoneCode(phone)
            _phoneCodeSuccessLiveData.postValue(true)
        }
    }

    fun saveUserEmail(email: String, code: String) {
        doAsync {
            personalDataUseCase.saveUserEmail(email, code)
            _emailSaveSuccessLiveData.postValue(true)
        }
    }

    fun saveUserPhone(phone: String, code: String) {
        doAsync {
            personalDataUseCase.saveUserPhone(phone, code)
            _phoneSaveSuccessLiveData.postValue(true)
        }
    }
}