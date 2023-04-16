package com.bestDate.presentation.main.userProfile.likesList

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LikesListViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    likesUseCase: LikesListUseCase
) : BaseViewModel() {

    var likesList = likesUseCase.likesList.asLiveData()
        .cachedIn(viewModelScope)

    fun likesAreViewed() {
        userUseCase.hasNewLikes.postValue(false)
    }
}