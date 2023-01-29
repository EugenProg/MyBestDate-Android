package com.bestDate.presentation.main.userProfile.matchesList

import com.bestDate.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchesListViewModel @Inject constructor(
    private val matchesUseCase: MatchesListUseCase
) : BaseViewModel() {

    var matchesList = matchesUseCase.matchesList

    fun getMatches() {
        doAsync {
            matchesUseCase.getMatches()
        }
    }
}