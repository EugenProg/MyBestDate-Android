package com.bestDate.presentation.questionnarie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.base.BaseViewModel
import com.bestDate.db.entity.QuestionnaireDB
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionnaireViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): BaseViewModel() {

    val user = userUseCase.getMyUser.asLiveData()

    private var _questionnaireUseCase = MutableLiveData<Boolean>()
    var questionnaireUseCase: LiveData<Boolean> = _questionnaireUseCase

    fun saveQuestionnaire(questionnaire: QuestionnaireDB) {
        doAsync {
            userUseCase.saveQuestionnaire(questionnaire)
            _questionnaireUseCase.postValue(true)
        }
    }

}