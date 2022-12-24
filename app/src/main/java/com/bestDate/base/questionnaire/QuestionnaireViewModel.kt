package com.bestDate.base.questionnaire

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
) : BaseViewModel() {

    val user = userUseCase.getMyUser.asLiveData()

    private var _questionnaireSaveLiveData = MutableLiveData<Boolean>()
    var questionnaireSaveLiveData: LiveData<Boolean> = _questionnaireSaveLiveData

    fun saveQuestionnaire(questionnaire: QuestionnaireDB) {
        doAsync {
            userUseCase.saveQuestionnaire(questionnaire)
            userUseCase.refreshUser()
            _questionnaireSaveLiveData.postValue(true)
        }
    }
}