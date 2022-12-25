package com.bestDate.presentation.main.matches

import androidx.lifecycle.asLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()

}