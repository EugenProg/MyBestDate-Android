package com.bestDate.presentation.main.userProfile.matchesList

import androidx.lifecycle.MutableLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.data.model.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchesListViewModel @Inject constructor(
    private val matchesUseCase: MatchesListUseCase
): BaseViewModel() {

    var matchesList: MutableLiveData<MutableList<Match>> = matchesUseCase.matchesList

    fun getMatches() {
        doAsync {
            matchesUseCase.getMatches()
        }
    }
}