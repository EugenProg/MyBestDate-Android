package com.bestDate.presentation.main.userProfile.matchesList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.data.model.Match
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchesListViewModel @Inject constructor(
    private val matchesUseCase: MatchesListUseCase,
    userUseCase: UserUseCase
): BaseViewModel() {

    var matchesList: MutableLiveData<MutableList<Match>> = matchesUseCase.matchesList
    var myUser = userUseCase.getMyUser.asLiveData()

    fun getMatches() {
        doAsync {
            matchesUseCase.getMatches()
        }
    }
}