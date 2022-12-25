package com.bestDate.presentation.messageSettings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.data.model.SettingsType
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.userProfile.settings.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageSettingsViewModel @Inject constructor(
    private var settingsUseCase: SettingsUseCase,
    private val preferencesUtils: PreferencesUtils
) : BaseViewModel() {

    var userSettings = settingsUseCase.getUserSettings.asLiveData()

    private var _saveLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var saveLiveData: LiveData<Boolean> = _saveLiveData

    fun save(enabled: Boolean) {
        doAsync {
            settingsUseCase.updateUserSettings(SettingsType.MESSAGES, enabled)
            _saveLiveData.postValue(true)
        }
    }

    fun setNotFirstEnter() {
        preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, false)
    }
}