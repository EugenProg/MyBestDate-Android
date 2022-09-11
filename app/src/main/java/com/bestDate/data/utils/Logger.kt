package com.bestDate.data.utils

import timber.log.Timber

object Logger {

    fun print(message: String) {
        Timber.e(message)
    }
}