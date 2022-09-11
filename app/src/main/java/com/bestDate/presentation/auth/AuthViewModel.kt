package com.bestDate.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.R
import com.bestDate.base.BaseViewModel
import com.bestDate.data.extension.formatToPhoneNumber
import com.bestDate.data.extension.isAEmail
import com.bestDate.data.extension.isPhoneNumber
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase): BaseViewModel() {

    private var _loginLiveData = MutableLiveData<Boolean>()
    val loginLiveData: LiveData<Boolean> = _loginLiveData

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
            userUseCase.refreshUser()
            _loginLiveData.postValue(true)
        }
    }

    private fun loginByPhone(login: String, password: String) {
        doAsync {
            authUseCase.loginByPhone(login.formatToPhoneNumber(), password)
            userUseCase.refreshUser()
            _loginLiveData.postValue(true)
        }
    }
}