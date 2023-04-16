package com.bestDate.presentation.main.userProfile.matchesList

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchesListViewModel @Inject constructor(
    matchesUseCase: MatchesListUseCase,
    private val userUseCase: UserUseCase
) : BaseViewModel() {

    var matchesList = matchesUseCase.matchesList.asLiveData()
        .cachedIn(viewModelScope)

    val userPhoto = userUseCase.userMainPhotoUrl

    fun areMatchesViewed() {
        userUseCase.hasNewMatches.postValue(false)
    }
}