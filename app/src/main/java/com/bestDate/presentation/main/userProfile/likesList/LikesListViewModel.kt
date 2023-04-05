package com.bestDate.presentation.main.userProfile.likesList

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bestDate.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LikesListViewModel @Inject constructor(likesUseCase: LikesListUseCase) : BaseViewModel() {

    var likesList = likesUseCase.likesList.asLiveData()
        .cachedIn(viewModelScope)
}