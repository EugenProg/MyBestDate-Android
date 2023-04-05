package com.bestDate.presentation.main.userProfile.myDuels

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bestDate.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyDuelsViewModel @Inject constructor(
    myDuelsUseCase: MyDuelsUseCase
) : BaseViewModel() {

    var myDuels = myDuelsUseCase.myDuels.asLiveData()
        .cachedIn(viewModelScope)

}