package com.bestDate.presentation.main.top

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.data.model.DuelProfile
import com.bestDate.data.model.ProfileImage
import com.bestDate.data.model.ShortUserData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.main.userProfile.myDuels.MyDuelsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val duelUseCase: DuelsUseCase
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()

    private var _duelsLiveData = MutableLiveData<MutableList<ProfileImage>?>()
    var duelsLiveData: LiveData<MutableList<ProfileImage>?> = _duelsLiveData

    private var _duelsResultLiveData = MutableLiveData<MutableList<DuelProfile>?>()
    var duelsResultLiveData: LiveData<MutableList<DuelProfile>?> = _duelsResultLiveData

    fun getDuels(gender: String, country: String?) {
        doAsync {
            duelUseCase.getMyDuels(gender, country)
            _duelsLiveData.postValue(duelUseCase.duelProfiles)
        }
    }

    fun postVote(winningPhoto: Int, loserPhoto: Int) {
        doAsync {
            duelUseCase.postVote(winningPhoto, loserPhoto)
            _duelsResultLiveData.postValue(duelUseCase.duelResults)
        }
    }

}