package com.bestDate.presentation.main.userProfile.settings

import androidx.lifecycle.asLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.data.model.SettingsType
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCase: SettingsUseCase,
    private val userUseCase: UserUseCase
): BaseViewModel() {

    var user = userUseCase.getMyUser.asLiveData()
    var userSettings = settingsUseCase.getUserSettings.asLiveData()

    fun refreshUserSettings() {
        doAsync {
            settingsUseCase.refreshUserSettings()
        }
    }

    fun updateUserSettings(type: SettingsType, checked: Boolean) {
        doAsync {
            settingsUseCase.updateUserSettings(type, checked)
        }
    }
}