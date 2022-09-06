package com.bestDate.fragment.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.R
import com.bestDate.base.BaseViewModel
import com.bestDate.data.extension.formatToPhoneNumber
import com.bestDate.data.extension.isAEmail
import com.bestDate.data.extension.isPhoneNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase): BaseViewModel() {

    private var _loginByPhoneLiveData = MutableLiveData<Boolean>()
    val loginByPhoneLiveData: LiveData<Boolean> = _loginByPhoneLiveData

    private var _loginByEmailLiveData = MutableLiveData<Boolean>()
    val loginByEmailLiveData: LiveData<Boolean> = _loginByEmailLiveData

    private var _validationErrorLiveData = MutableLiveData<Int>()
    val validationErrorLiveData: LiveData<Int> = _validationErrorLiveData

    fun logIn(login: String, password: String) {
        when {
            login.isPhoneNumber() -> loginByPhone(login, password)
            login.isAEmail() -> loginByEmail(login, password)
            else -> _validationErrorLiveData.value = R.string.enter_email_or_phone
        }
    }

    private fun loginByEmail(login: String, password: String) {
        doAsync {
            authUseCase.loginByEmail(login.trim(), password)
            _loginByEmailLiveData.postValue(true)
        }
    }

    private fun loginByPhone(login: String, password: String) {
        doAsync {
            authUseCase.loginByPhone(login.formatToPhoneNumber(), password)
            _loginByPhoneLiveData.postValue(true)
        }
    }
}