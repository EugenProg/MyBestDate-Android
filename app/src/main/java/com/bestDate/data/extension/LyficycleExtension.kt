package com.bestDate.data.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

inline fun LifecycleOwner.postDelayed(crossinline body: suspend CoroutineScope.() -> Unit, delay: Long) {
    lifecycleScope.launchWhenStarted {
        delay(delay)
        body.invoke(this)
    }
}