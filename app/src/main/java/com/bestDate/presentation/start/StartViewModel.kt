package com.bestDate.presentation.start

import androidx.lifecycle.asLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.data.utils.notifications.PusherCenter
import com.bestDate.presentation.auth.AuthUseCase
import com.bestDate.presentation.main.UserUseCase
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val authUseCase: AuthUseCase,
    private val preferencesUtils: PreferencesUtils,
    private val pusherCenter: PusherCenter
) : BaseViewModel() {

    var user = userUseCase.getMyUser.asLiveData()

    private var _refreshSuccessLiveData: LiveEvent<Boolean> = LiveEvent()
    var refreshSuccessLiveData: LiveEvent<Boolean> = _refreshSuccessLiveData

    private var _updateLanguageSuccessLiveData: LiveEvent<Boolean> = LiveEvent()
    var updateLanguageSuccessLiveData: LiveEvent<Boolean> = _updateLanguageSuccessLiveData

    fun refreshToken() {
        doAsync {
            authUseCase.refreshToken()
            _refreshSuccessLiveData.postValue(true)
        }
    }

    fun isFirstEnter() = preferencesUtils.getBooleanWithDefault(Preferences.FIRST_ENTER, true)

    fun isRefreshTokenValid() = preferencesUtils.getString(Preferences.REFRESH_TOKEN).isNotBlank()

    fun getSkipQuestionnaireCount() = preferencesUtils.getInt(Preferences.QUESTIONNAIRE_SKIP_COUNT)

    fun getSkipImageCount() = preferencesUtils.getInt(Preferences.IMAGE_SKIP_COUNT)

    fun refreshUser() {
        doAsync {
            userUseCase.refreshUser()
        }
    }

    fun changeLanguage(language: String) {
        doAsync {
            userUseCase.changeLanguage(language)
            _updateLanguageSuccessLiveData.postValue(true)
        }
    }

    fun startPusher() {
        doAsync {
            pusherCenter.startPusher()
        }
    }
}