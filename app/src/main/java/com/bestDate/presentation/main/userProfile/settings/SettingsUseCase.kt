package com.bestDate.presentation.main.userProfile.settings

import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.InternalException
import com.bestDate.data.model.SettingsType
import com.bestDate.db.dao.UserSettingsDao
import com.bestDate.network.remote.UserRemoteData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsUseCase @Inject constructor(
    private val userSettingsDao: UserSettingsDao,
    private val userRemoteData: UserRemoteData
) {

    var getUserSettings = userSettingsDao.getUserSettingsFlow()

    suspend fun refreshUserSettings() {
        val response = userRemoteData.getUserSettings()
        if (response.isSuccessful) {
            response.body()?.let {
                userSettingsDao.validate(it.data)
            }
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }

    suspend fun updateUserSettings(type: SettingsType, checked: Boolean) {
        val response = userRemoteData.updateUserSettings(type, checked)
        if (response.isSuccessful) {
            response.body()?.let {
                userSettingsDao.validate(it.data)
            }
        } else throw InternalException.OperationException(response.errorBody().getErrorMessage())
    }
}