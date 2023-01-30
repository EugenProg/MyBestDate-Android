package com.bestDate.presentation.main.duels.top

import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.model.DuelProfile
import com.bestDate.data.model.InternalException
import com.bestDate.network.remote.TopRemoteData
import com.bestDate.presentation.registration.Gender
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopUseCase @Inject constructor(
    private val topRemoteData: TopRemoteData
) {
    var topsResults: MutableLiveData<MutableList<DuelProfile>?> = MutableLiveData()
    var genderLocal: MutableLiveData<Gender> = MutableLiveData()
    private var topsWoman: MutableList<DuelProfile> = mutableListOf()
    private var topsMan: MutableList<DuelProfile> = mutableListOf()

    suspend fun getTop(gender: Gender) {
        when {
            gender == Gender.WOMAN && topsWoman.isNotEmpty() -> {
                topsResults.postValue(topsWoman)
                genderLocal.postValue(gender)
            }
            gender == Gender.MAN && topsMan.isNotEmpty() -> {
                topsResults.postValue(topsMan)
                genderLocal.postValue(gender)
            }
            else -> {
                val response = topRemoteData.getTop(gender.serverName)
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (gender == Gender.WOMAN) {
                            topsWoman = it.data ?: mutableListOf()
                            topsResults.postValue(topsWoman)
                        } else {
                            topsMan = it.data ?: mutableListOf()
                            topsResults.postValue(topsMan)
                        }
                        genderLocal.postValue(gender)
                    }
                } else
                    throw InternalException.OperationException(
                        response.errorBody()?.getErrorMessage()
                    )
            }
        }
    }

    fun clearData() {
        topsResults.postValue(mutableListOf())
        topsWoman = mutableListOf()
        topsMan = mutableListOf()
        genderLocal = MutableLiveData()
    }
}