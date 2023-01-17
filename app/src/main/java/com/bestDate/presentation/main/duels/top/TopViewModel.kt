package com.bestDate.presentation.main.duels.top

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import javax.inject.Inject

class TopViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val topUseCase: TopUseCase
) : BaseViewModel() {

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun getDuels(gender: String, country: String?) {
        _loadingLiveData.postValue(true)
        doAsync {
            topUseCase.getTop(gender, country)
           // _duelsLiveData.postValue(duelUseCase.duelProfiles)
            _loadingLiveData.postValue(false)
        }
    }
}