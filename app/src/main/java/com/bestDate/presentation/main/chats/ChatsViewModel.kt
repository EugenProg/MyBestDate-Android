package com.bestDate.presentation.main.chats

import androidx.lifecycle.asLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()
}