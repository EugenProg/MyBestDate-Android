package com.bestDate.presentation.main.duels.top

import androidx.lifecycle.asLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.main.duels.DuelsUseCase
import com.bestDate.presentation.registration.Gender
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val topUseCase: TopUseCase,
    private val duelsUseCase: DuelsUseCase
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()
    var gender: Gender
        get() = duelsUseCase.gender
        set(value) {
            duelsUseCase.gender = value
        }
    val topsMan = topUseCase.topsMan
    val topsWoman = topUseCase.topsWoman

    fun getTop() {
        doAsync {
            topUseCase.getTop(gender)
        }
    }
}