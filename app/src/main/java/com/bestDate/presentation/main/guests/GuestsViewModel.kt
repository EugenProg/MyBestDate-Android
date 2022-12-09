package com.bestDate.presentation.main.guests

import androidx.lifecycle.asLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GuestsViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()

}