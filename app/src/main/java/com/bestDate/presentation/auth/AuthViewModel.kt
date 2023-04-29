package com.bestDate.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.R
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.data.extension.formatToPhoneNumber
import com.bestDate.data.extension.isAEmail
import com.bestDate.data.extension.isPhoneNumber
import com.bestDate.data.model.SocialProvider
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.data.utils.notifications.PusherCenter
import com.bestDate.db.entity.UserDB
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.main.chats.ChatListUseCase
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase,
    private val chatListUseCase: ChatListUseCase,
    private val pusherCenter: PusherCenter,
    private val preferencesUtils: PreferencesUtils
) : BaseViewModel() {

    var registrationSocialMode: Boolean
    get() = authUseCase.registrationSocialMode
    set(value) {
        authUseCase.registrationSocialMode = value
    }

    private var _validationErrorLiveData = LiveEvent<Int>()
    val validationErrorLiveData: LiveEvent<Int> = _validationErrorLiveData

    private var _loginProcessLiveData = LiveEvent<Boolean>()
    val loginProcessLiveData: LiveEvent<Boolean> = _loginProcessLiveData

    private var _loginSuccessLiveData = MutableLiveData<UserDB?>()
    val loginSuccessLiveData: LiveData<UserDB?> = _loginSuccessLiveData

    var user = userUseCase.getMyUser.asLiveData()

    fun getSkipQuestionnaireCount() = preferencesUtils.getInt(Preferences.QUESTIONNAIRE_SKIP_COUNT)

    fun getSkipImageCount() = preferencesUtils.getInt(Preferences.IMAGE_SKIP_COUNT)

    fun logIn(login: String, password: String, appLanguage: String) {
        when {
            login.isPhoneNumber() -> loginByPhone(login, password, appLanguage)
            login.isAEmail() -> loginByEmail(login, password, appLanguage)
            else -> _validationErrorLiveData.value = R.string.enter_email_or_phone
        }
    }

    private fun loginByEmail(login: String, password: String, appLanguage: String) {
        _loginProcessLiveData.value = true
        doAsync {
            authUseCase.loginByEmail(login.trim(), password)
            userUseCase.changeLanguage(appLanguage)
            chatListUseCase.refreshChatList()
            pusherCenter.startPusher()
            preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, false)
            _loginProcessLiveData.postValue(false)
            _loginSuccessLiveData.postValue(user.value)
        }
    }

    private fun loginByPhone(login: String, password: String, appLanguage: String) {
        _loginProcessLiveData.value = true
        doAsync {
            authUseCase.loginByPhone(login.formatToPhoneNumber(), password)
            userUseCase.changeLanguage(appLanguage)
            chatListUseCase.refreshChatList()
            pusherCenter.startPusher()
            preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, false)
            _loginProcessLiveData.postValue(false)
            _loginSuccessLiveData.postValue(user.value)
        }
    }

    fun loginSocial(provider: SocialProvider, token: String?, appLanguage: String) {
        doAsync {
            authUseCase.loginSocial(provider, token)
            userUseCase.changeLanguage(appLanguage)
            chatListUseCase.refreshChatList()
            pusherCenter.startPusher()
            preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, registrationSocialMode)
            _loginSuccessLiveData.postValue(user.value)
        }
    }

    fun loginByGoogle(authCode: String?, appLanguage: String) {
        doAsync {
            authUseCase.loginWithGoogle(authCode)
            userUseCase.changeLanguage(appLanguage)
            chatListUseCase.refreshChatList()
            pusherCenter.startPusher()
            preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, registrationSocialMode)
            _loginSuccessLiveData.postValue(user.value)
        }
    }
}