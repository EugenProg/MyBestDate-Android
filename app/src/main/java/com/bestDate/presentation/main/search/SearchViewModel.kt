package com.bestDate.presentation.main.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.R
import com.bestDate.base.BaseViewModel
import com.bestDate.data.model.FilterOptions
import com.bestDate.data.model.ShortUserData
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val preferencesUtils: PreferencesUtils
) : BaseViewModel() {

    val user = userUseCase.getMyUser.asLiveData()
    var perPage: Int = 0
    var total: Int = 0

    private var _usersListLiveData = MutableLiveData<MutableList<ShortUserData>>()
    var usersListLiveData: LiveData<MutableList<ShortUserData>> = _usersListLiveData

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun getUsers(filters: FilterOptions) {
        _loadingLiveData.postValue(true)
        doAsync {
            userUseCase.getUsers(filters)
            perPage = userUseCase.perPage
            total = userUseCase.total
            _usersListLiveData.postValue(userUseCase.usersList)
            _loadingLiveData.postValue(false)
        }
    }

    fun getUsersPaged(filters: FilterOptions) {
        _loadingLiveData.postValue(true)
        doAsync {
            userUseCase.getUsersPaged(filters)
            _usersListLiveData.postValue(userUseCase.usersList)
            _loadingLiveData.postValue(false)
        }
    }

    fun getLocationMap(context: Context): MutableList<Pair<String, String>> {
        val country = user.value?.location?.country ?: ""
        val city = user.value?.location?.city ?: ""
        return mutableListOf(
            context.getString(R.string.next_to_me) to "nearby",
            context.getString(R.string.all_world) to "all",
            country to "country",
            city to "city"
        )
    }

    fun getStatusesMap(context: Context) = mutableListOf(
        context.getString(R.string.online) to "online",
        context.getString(R.string.not_selected) to "all",
        context.getString(R.string.was_recently) to "recently"
    )

    fun clearData() {
        userUseCase.clearPagingData()
    }

    fun saveFilter(type: Preferences, value: String) {
        preferencesUtils.saveString(type, value)
    }

    fun getFilter(type: Preferences): String {
        val value = preferencesUtils.getString(type)
        return value.ifEmpty { "all" }
    }

    fun setNotFirstEnter() {
        preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, false)
    }
}