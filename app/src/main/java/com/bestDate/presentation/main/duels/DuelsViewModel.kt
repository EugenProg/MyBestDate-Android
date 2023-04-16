package com.bestDate.presentation.main.duels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.registration.Gender
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DuelsViewModel @Inject constructor(
    userUseCase: UserUseCase,
    private val duelUseCase: DuelsUseCase
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()
    val coins = userUseCase.coinsCount
    val hasNewDuels = userUseCase.hasNewDuels
    val duelImages = duelUseCase.duelImages
    val duelResults = duelUseCase.duelResults
    var gender: Gender
        get() = duelUseCase.gender
        set(value) {
            duelUseCase.gender = value
        }

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private var _genderIsSetLiveData = MutableLiveData<Boolean>()
    val genderIsSetLiveData: LiveData<Boolean> = _genderIsSetLiveData

    fun getDuels() {
        _loadingLiveData.postValue(true)
        doAsync {
            duelUseCase.getMyDuels()
            _loadingLiveData.postValue(false)
        }
    }

    fun postVote(winningPhoto: Int, loserPhoto: Int) {
        doAsync {
            duelUseCase.postVote(winningPhoto, loserPhoto)
            duelUseCase.getMyDuels()
        }
    }

    fun setUserGender() {
        doAsync {
            duelUseCase.setUserGender()
            _genderIsSetLiveData.postValue(true)
            getDuels()
        }
    }
}