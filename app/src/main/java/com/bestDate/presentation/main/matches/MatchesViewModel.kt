package com.bestDate.presentation.main.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.data.extension.orZero
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.base.anotherProfile.AnotherProfileUseCase
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val matchUseCase: MatchUseCase,
    private val anotherProfileUseCase: AnotherProfileUseCase,
    private val preferencesUtils: PreferencesUtils
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()
    val matchesList = matchUseCase.matchesList
    val matchAction = matchUseCase.matchAction

    private var _getUserLiveData: MutableLiveData<Boolean> = LiveEvent()
    var getUserLiveData: LiveData<Boolean> = _getUserLiveData

    fun getMatches() {
        if (matchesList.value.isNullOrEmpty()) {
            doAsync {
                matchUseCase.getUsersForMatch()
            }
        }
    }

    fun listIsEmpty(): Boolean = matchUseCase.fullList.isEmpty()

    fun matchesEnabled(): Boolean = preferencesUtils.getBoolean(Preferences.MATCHES_ENABLED)

    fun matchAction(userId: Int?) {
        doAsync {
            matchUseCase.matchAction(userId.orZero)
        }
    }

    fun nextUser() {
        matchUseCase.nextUser()
    }

    fun getUserById(userId: Int?) {
        doAsync {
            anotherProfileUseCase.getUserById(userId)
            _getUserLiveData.postValue(true)
        }
    }

    fun clearMatch() {
        matchUseCase.clearMatch()
    }
}