package com.bestDate.presentation.main.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.R
import com.bestDate.data.model.FilterOptions
import com.bestDate.data.model.Meta
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.data.utils.CityListItem
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.presentation.main.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val searchUseCase: SearchUseCase,
    private val preferencesUtils: PreferencesUtils,
    private val geoLocationUseCase: GeoLocationUseCase
) : BaseViewModel() {

    val user = userUseCase.getMyUser.asLiveData()
    val userList = searchUseCase.usersList
    var meta: Meta? = Meta()

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData
    val locationLiveData = geoLocationUseCase.locationLiveData

    fun getUsers(filters: FilterOptions) {
        _loadingLiveData.postValue(true)
        doAsync {
            searchUseCase.getUsers(filters)
            meta = searchUseCase.meta
            _loadingLiveData.postValue(false)
        }
    }

    fun getLocationByAddress(location: CityListItem?) {
        doAsync {
            geoLocationUseCase.getLocationByAddress(location)
        }
    }

    fun getUsersPaged(filters: FilterOptions) {
        _loadingLiveData.postValue(true)
        doAsync {
            searchUseCase.getUsersPaged(filters)
            _loadingLiveData.postValue(false)
        }
    }

    fun getLocationMap(context: Context): LinkedHashMap<FilterType, String> {
        val country = user.value?.location?.country ?: ""
        val city = user.value?.location?.city ?: ""
        return linkedMapOf(
            FilterType.NEARBY to context.getString(R.string.next_to_me),
            FilterType.ALL to context.getString(R.string.all_world),
            FilterType.COUNTRY to country,
            FilterType.CITY to city
        )
    }

    fun getStatusesMap(context: Context) = linkedMapOf(
        FilterType.ONLINE to context.getString(R.string.online),
        FilterType.NOT_SELECTED to context.getString(R.string.not_selected),
        FilterType.RECENTLY to context.getString(R.string.was_recently)
    )

    fun clearData() {
        searchUseCase.clearPagingData()
    }

    fun saveFilter(type: Preferences, value: FilterType) {
        preferencesUtils.saveString(type, value.name)
    }

    fun getFilter(type: Preferences): FilterType {
        val value = FilterType.values().firstOrNull {
            it.name == preferencesUtils.getString(type)
        } ?: if (type == Preferences.FILTER_LOCATION) FilterType.ALL else FilterType.NOT_SELECTED
        return value
    }
}

enum class FilterType(var serverName: String) {
    NEARBY("nearby"),
    ALL("all"),
    COUNTRY("country"),
    CITY("city"),
    ONLINE("online"),
    NOT_SELECTED("all"),
    RECENTLY("recently")
}