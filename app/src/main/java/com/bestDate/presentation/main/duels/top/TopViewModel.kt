package com.bestDate.presentation.main.duels.top

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.main.duels.DuelsUseCase
import com.bestDate.presentation.registration.Gender
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopViewModel @Inject constructor(
    userUseCase: UserUseCase,
    topUseCase: TopUseCase,
    private val duelsUseCase: DuelsUseCase
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()
    var gender: Gender
        get() = duelsUseCase.gender
        set(value) {
            duelsUseCase.gender = value
        }
    val topsMan = topUseCase.topsMan.asLiveData()
        .cachedIn(viewModelScope)

    val topsWoman = topUseCase.topsWoman.asLiveData()
        .cachedIn(viewModelScope)
}