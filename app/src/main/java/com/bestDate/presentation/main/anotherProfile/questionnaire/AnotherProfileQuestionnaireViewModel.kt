package com.bestDate.presentation.main.anotherProfile.questionnaire

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.main.anotherProfile.AnotherProfileUseCase
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnotherProfileQuestionnaireViewModel @Inject constructor(
    private val anotherProfileUseCase: AnotherProfileUseCase,
    userUseCase: UserUseCase
) : BaseViewModel() {

    val myUser = userUseCase.getMyUser.asLiveData()
    val user = anotherProfileUseCase.user

    private var _translateLiveData: MutableLiveData<String?> = LiveEvent()
    var translateLiveData: LiveData<String?> = _translateLiveData

    fun translate() {
        doAsync {
            anotherProfileUseCase.translate()
            _translateLiveData.postValue(anotherProfileUseCase.translatedText)
        }
    }
}