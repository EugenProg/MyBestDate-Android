package com.bestDate.presentation.main.search

import com.bestDate.base.BaseViewModel
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val preferencesUtils: PreferencesUtils
) : BaseViewModel() {

    fun setNotFirstEnter() {
        preferencesUtils.saveBoolean(Preferences.FIRST_ENTER, false)
    }
}