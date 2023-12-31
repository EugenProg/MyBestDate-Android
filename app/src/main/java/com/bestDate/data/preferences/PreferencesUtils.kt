package com.bestDate.data.preferences

import android.content.SharedPreferences

class PreferencesUtils(private var preferences: SharedPreferences) {

    fun getString(key: Preferences): String {
        return preferences.getString(key.name, "").orEmpty()
    }

    fun getLong(key: Preferences): Long {
        return preferences.getLong(key.name, 0)
    }

    fun getInt(key: Preferences): Int {
        return preferences.getInt(key.name, 0)
    }

    fun getBoolean(key: Preferences): Boolean {
        return preferences.getBoolean(key.name, false)
    }

    fun getBooleanWithDefault(key: Preferences, default: Boolean): Boolean {
        return preferences.getBoolean(key.name, default)
    }

    fun saveString(key: Preferences, value: String) {
        val editor = preferences.edit()
        editor.putString(key.name, value)
        editor.apply()
    }

    fun saveInt(key: Preferences, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key.name, value)
        editor.apply()
    }

    fun saveBoolean(key: Preferences, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key.name, value)
        editor.apply()
    }

    fun saveLong(key: Preferences, value: Long) {
        val editor = preferences.edit()
        editor.putLong(key.name, value)
        editor.apply()
    }
}