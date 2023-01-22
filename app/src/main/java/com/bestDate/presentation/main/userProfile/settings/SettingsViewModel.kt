package com.bestDate.presentation.main.userProfile.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.data.model.SettingsType
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.view.bottomSheet.languageSheet.LanguageType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCase: SettingsUseCase,
    private val userUseCase: UserUseCase
): BaseViewModel() {

    var userSettings = settingsUseCase.getUserSettings.asLiveData()

    private var _deleteSuccessLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var deleteSuccessLiveData: LiveData<Boolean> = _deleteSuccessLiveData

    private var _languageSaveLiveData: MutableLiveData<LanguageType> = MutableLiveData()
    var languageSaveLiveData: LiveData<LanguageType> = _languageSaveLiveData

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

    fun deleteUserProfile() {
        doAsync {
            userUseCase.deleteUserProfile()
            _deleteSuccessLiveData.postValue(true)
        }
    }

    fun saveLanguage(languageType: LanguageType) {
        doAsync {
            userUseCase.changeLanguage(languageType.settingsName)
            _languageSaveLiveData.postValue(languageType)
        }
    }
}