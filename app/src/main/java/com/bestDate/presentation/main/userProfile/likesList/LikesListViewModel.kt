package com.bestDate.presentation.main.userProfile.likesList

import androidx.lifecycle.MutableLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.data.model.Like
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LikesListViewModel @Inject constructor(
    private val likesUseCase: LikesListUseCase
): BaseViewModel() {

    var likesList: MutableLiveData<MutableList<Like>> = likesUseCase.likesList

    fun getLikes() {
        doAsync {
            likesUseCase.getLikes()
        }
    }
}