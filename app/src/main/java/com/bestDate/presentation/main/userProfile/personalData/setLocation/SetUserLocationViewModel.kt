package com.bestDate.presentation.main.userProfile.personalData.setLocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.data.utils.CityListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SetUserLocationViewModel @Inject constructor(
    private val setUserLocationUseCase: SetUserLocationUseCase
) : BaseViewModel() {

    private var _saveSuccessfulLivaData: MutableLiveData<Boolean> = MutableLiveData()
    var saveSuccessfulLivaData: LiveData<Boolean> = _saveSuccessfulLivaData

    fun saveUserLocation(location: CityListItem) {
        doAsync {
            setUserLocationUseCase.saveUserLocation(location)
            _saveSuccessfulLivaData.postValue(true)
        }
    }
}