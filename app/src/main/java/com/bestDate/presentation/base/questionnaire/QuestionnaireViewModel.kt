package com.bestDate.presentation.base.questionnaire

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.db.entity.QuestionnaireDB
import com.bestDate.presentation.main.UserUseCase
import com.bestDate.presentation.main.userProfile.personalData.PersonalDataUseCase
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionnaireViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val personalDataUseCase: PersonalDataUseCase
) : BaseViewModel() {

    val user = userUseCase.getMyUser.asLiveData()

    private var _questionnaireSaveLiveData = MutableLiveData<Boolean>()
    var questionnaireSaveLiveData: LiveData<Boolean> = _questionnaireSaveLiveData

    private var _emailCodeSuccessLiveData: LiveEvent<Boolean> = LiveEvent()
    var emailCodeSuccessLiveData: LiveEvent<Boolean> = _emailCodeSuccessLiveData

    private var _emailSaveSuccessLiveData: LiveEvent<Boolean> = LiveEvent()
    var emailSaveSuccessLiveData: LiveEvent<Boolean> = _emailSaveSuccessLiveData

    private var _phoneCodeSuccessLiveData: LiveEvent<Boolean> = LiveEvent()
    var phoneCodeSuccessLiveData: LiveEvent<Boolean> = _phoneCodeSuccessLiveData

    private var _phoneSaveSuccessLiveData: LiveEvent<Boolean> = LiveEvent()
    var phoneSaveSuccessLiveData: LiveEvent<Boolean> = _phoneSaveSuccessLiveData

    fun saveQuestionnaire(questionnaire: QuestionnaireDB) {
        doAsync {
            userUseCase.saveQuestionnaire(questionnaire)
            userUseCase.refreshUser()
            _questionnaireSaveLiveData.postValue(true)
        }
    }

    fun sendEmailCode(email: String) {
        doAsync {
            personalDataUseCase.sendEmailCode(email)
            _emailCodeSuccessLiveData.postValue(true)
        }
    }

    fun sendPhoneCode(phone: String) {
        doAsync {
            personalDataUseCase.sendPhoneCode(phone)
            _phoneCodeSuccessLiveData.postValue(true)
        }
    }

    fun saveUserEmail(email: String, code: String) {
        doAsync {
            personalDataUseCase.saveUserEmail(email, code)
            _emailSaveSuccessLiveData.postValue(true)
        }
    }

    fun saveUserPhone(phone: String, code: String) {
        doAsync {
            personalDataUseCase.saveUserPhone(phone, code)
            _phoneSaveSuccessLiveData.postValue(true)
        }
    }
}