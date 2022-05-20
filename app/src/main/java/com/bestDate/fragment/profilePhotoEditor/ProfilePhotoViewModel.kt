package com.bestDate.fragment.profilePhotoEditor

import com.bestDate.base.BaseViewModel
import com.bestDate.data.model.Image
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfilePhotoViewModel @Inject constructor(): BaseViewModel() {

    private var _photoSaveLiveData: LiveEvent<Image> = LiveEvent()
    var photoSaveLiveData: LiveEvent<Image> = _photoSaveLiveData

}