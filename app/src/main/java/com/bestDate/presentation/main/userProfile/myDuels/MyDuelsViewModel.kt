package com.bestDate.presentation.main.userProfile.myDuels

import androidx.lifecycle.MutableLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.data.model.MyDuel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyDuelsViewModel @Inject constructor(
    private val myDuelsUseCase: MyDuelsUseCase
): BaseViewModel() {

    var myDuels: MutableLiveData<MutableList<MyDuel>> = myDuelsUseCase.myDuels

    fun getMyDuels() {
        doAsync {
            myDuelsUseCase.getMyDuels()
        }
    }
}