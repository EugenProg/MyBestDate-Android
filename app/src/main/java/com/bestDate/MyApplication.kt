package com.bestDate

import android.app.Application
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MyApplication: Application() {

    @Inject
    lateinit var preferencesUtils: PreferencesUtils

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        Lingver.init(this,
            preferencesUtils.getString(Preferences.LANGUAGE).ifBlank {
                getString(R.string.app_locale)
            }
        )
    }
}