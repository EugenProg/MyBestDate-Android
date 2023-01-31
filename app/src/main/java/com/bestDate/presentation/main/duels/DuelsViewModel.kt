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
    private val userUseCase: UserUseCase,
    private val duelUseCase: DuelsUseCase
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()
    val coins = userUseCase.coinsCount
    val duelImages = duelUseCase.duelProfiles
    val duelResults = duelUseCase.duelResults
    var gender = duelUseCase.genderLocal

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun getDuels(gender: Gender? = duelUseCase.genderLocal.value) {
        _loadingLiveData.postValue(true)
        doAsync {
            if (gender != null) {
                duelUseCase.getMyDuels(gender, null)
            }
            _loadingLiveData.postValue(false)
        }
    }

    fun postVote(winningPhoto: Int, loserPhoto: Int) {
        doAsync {
            duelUseCase.postVote(winningPhoto, loserPhoto)
            getDuels()
        }
    }
}