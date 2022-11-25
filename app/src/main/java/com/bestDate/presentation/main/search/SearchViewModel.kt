package com.bestDate.presentation.main.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.R
import com.bestDate.base.BaseViewModel
import com.bestDate.data.model.FilterOptions
import com.bestDate.data.model.ShortUserData
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : BaseViewModel() {

    val user = userUseCase.getMyUser.asLiveData()

    private var _usersListLiveData = MutableLiveData<MutableList<ShortUserData>>()
    var usersListLiveData: LiveData<MutableList<ShortUserData>> = _usersListLiveData

    fun getUsers(filters: FilterOptions) {
        doAsync {
            userUseCase.getUsers(filters)
            _usersListLiveData.postValue(userUseCase.usersList)
        }
    }

    fun getLocationMap(context: Context): HashMap<String, String> {
        val country = user.value?.location?.country ?: ""
        val city = user.value?.location?.city ?: ""
        return hashMapOf(
            context.getString(R.string.next_to_me) to "nearby",
            context.getString(R.string.all_world) to "all",
            country to "country",
            city to "city"
        )
    }

    fun getStatusesMap(context: Context) = hashMapOf(
        context.getString(R.string.online) to "online",
        context.getString(R.string.not_selected) to "all",
        context.getString(R.string.was_recently) to "recently"
    )

}