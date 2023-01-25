package com.bestDate.presentation.main.guests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.data.model.IdListRequest
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GuestsViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val guestsUseCase: GuestsUseCase
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()
    val guestsListNew = guestsUseCase.guestsListNew
    val guestsListPrev = guestsUseCase.guestsListPrev
    val guestsList = guestsUseCase.guestsList

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun getGuests() {
        _loadingLiveData.postValue(true)
        doAsync {
            guestsUseCase.getGuestsList()
            _loadingLiveData.postValue(false)
        }
    }

    fun refreshUser() {
        doAsync {
            userUseCase.refreshUser()
        }
    }

    fun markGuestsViewed(list: MutableList<Int?>) {
        doAsync {
            guestsUseCase.markGuestsViewed(
                IdListRequest(list)
            )
            userUseCase.refreshUser()
        }
    }
}