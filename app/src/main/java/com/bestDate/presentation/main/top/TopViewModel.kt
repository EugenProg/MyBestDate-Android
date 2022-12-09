package com.bestDate.presentation.main.top

import androidx.lifecycle.asLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()

}