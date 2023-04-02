package com.bestDate.presentation.main.duels

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.bestDate.data.extension.getErrorMessage
import com.bestDate.data.extension.toBitmap
import com.bestDate.data.model.*
import com.bestDate.db.dao.UserDao
import com.bestDate.network.remote.DuelsRemoteData
import com.bestDate.presentation.registration.Gender
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DuelsUseCase @Inject constructor(
    private val userDao: UserDao,
    private val duelsRemoteData: DuelsRemoteData
) {
    var duelImages: MutableLiveData<MutableList<Pair<Bitmap?, Int?>>> = MutableLiveData()
    var duelResults: MutableLiveData<MutableList<DuelProfile>?> = MutableLiveData()

    private var genderIsSetted: Boolean = false
    var gender: Gender = Gender.WOMAN

    suspend fun getMyDuels() {
        val response = duelsRemoteData.getDuels(gender.serverName, null)
        if (response.isSuccessful) {
            response.body()?.let {
                val list: MutableList<Pair<Bitmap?, Int?>> = mutableListOf()
                it.data?.firstOrNull()?.let { image ->
                    list.add(Pair(URL(image.thumb_url).toBitmap(), image.id))
                }
                it.data?.lastOrNull()?.let { image ->
                    list.add(Pair(URL(image.thumb_url).toBitmap(), image.id))
                }
                duelImages.postValue(list)
            }
        } else {
            if (response.code() == 404) {
                duelImages.postValue(mutableListOf())
            }
            else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
        }
    }

    suspend fun postVote(winningPhoto: Int, loserPhoto: Int) {
        val response = duelsRemoteData.postVote(winningPhoto, loserPhoto)
        if (response.isSuccessful) {
            response.body()?.let {
                duelResults.postValue(it.data)
            }
        } else throw InternalException.OperationException(response.errorBody()?.getErrorMessage())
    }

    fun clearData() {
        duelImages.postValue(mutableListOf())
        duelResults.postValue(mutableListOf())
    }

    fun setUserGender() {
        if (!genderIsSetted) {
            gender = userDao.getUser()?.getDuelGender() ?: Gender.WOMAN
            genderIsSetted = true
        }
    }
}