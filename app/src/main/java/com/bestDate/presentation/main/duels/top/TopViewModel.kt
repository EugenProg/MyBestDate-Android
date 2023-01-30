package com.bestDate.presentation.main.duels.top

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.registration.Gender
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val topUseCase: TopUseCase
) : BaseViewModel() {
    val user = userUseCase.getMyUser.asLiveData()
    var gender = topUseCase.genderLocal
    var country: String? = ""
    val topsResults = topUseCase.topsResults

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun getTop(gender: Gender? = topUseCase.genderLocal.value) {
        if (topsResults.value.isNullOrEmpty())
            _loadingLiveData.postValue(true)
        doAsync {
            if (gender != null) {
                topUseCase.getTop(gender, country)
            }
            _loadingLiveData.postValue(false)
        }
    }
}