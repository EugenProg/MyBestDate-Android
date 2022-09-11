package com.bestDate.presentation.profilePhotoEditor

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.data.extension.toByteArray
import com.bestDate.data.model.ProfileImage
import com.bestDate.presentation.main.UserUseCase
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfilePhotoViewModel @Inject constructor(
    private val photoUseCase: ProfilePhotoUseCase,
    private val userUseCase: UserUseCase
): BaseViewModel() {

    val user = userUseCase.getMyUser.asLiveData()

    private var _photoSaveLiveData: LiveEvent<ProfileImage> = LiveEvent()
    var photoSaveLiveData: LiveEvent<ProfileImage> = _photoSaveLiveData

    private var _updateSettingsLiveData: LiveEvent<Boolean> = LiveEvent()
    var updateSettingsLiveData: LiveEvent<Boolean> = _updateSettingsLiveData

    private var _deleteLiveData: LiveEvent<Boolean> = LiveEvent()
    var deleteLiveData: LiveEvent<Boolean> = _deleteLiveData

    private var _deleteLoadingLiveData = MutableLiveData<Boolean>()
    val deleteLoadingLiveData: LiveData<Boolean> = _deleteLoadingLiveData

    private var _updateLoadingLiveData = MutableLiveData<Boolean>()
    val updateLoadingLiveData: LiveData<Boolean> = _updateLoadingLiveData

    fun savePhoto(image: Bitmap) {
        doAsync {
            photoUseCase.saveUserPhoto(image.toByteArray())
            userUseCase.refreshUser()
            _photoSaveLiveData.postValue(photoUseCase.savedImage)
        }
    }

    fun updatePhotoStatus(id: Int, main: Boolean, top: Boolean) {
        _updateLoadingLiveData.value = true
        doAsync {
            photoUseCase.updatePhotoStatus(id, main, top)
            userUseCase.refreshUser()
            _updateSettingsLiveData.postValue(true)
            _updateLoadingLiveData.postValue(false)
        }
    }

    fun deletePhoto(id: Int) {
        _deleteLoadingLiveData.value = true
        doAsync {
            photoUseCase.deletePhoto(id)
            userUseCase.refreshUser()
            _deleteLiveData.postValue(true)
            _deleteLoadingLiveData.postValue(false)
        }
    }
}