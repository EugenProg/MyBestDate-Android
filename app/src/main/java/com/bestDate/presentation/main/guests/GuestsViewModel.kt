package com.bestDate.presentation.main.guests

import androidx.lifecycle.asLiveData
import com.bestDate.data.model.IdListRequest
import com.bestDate.data.model.Meta
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GuestsViewModel @Inject constructor(
    userUseCase: UserUseCase,
    private val guestsUseCase: GuestsUseCase
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()
    val guestsList = guestsUseCase.guestsList
    var meta: Meta = Meta()

    fun getGuests() {
        doAsync {
            guestsUseCase.getGuestsList()
            meta = guestsUseCase.meta
        }
    }

    fun loadNextPage() {
        doAsync {
            guestsUseCase.getNextPage()
            meta = guestsUseCase.meta
        }
    }

    fun markGuestsViewed(list: MutableList<Int?>) {
        doAsync {
            guestsUseCase.markGuestsViewed(IdListRequest(list))
        }
    }
}