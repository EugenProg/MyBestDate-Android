package com.bestDate.data.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bestDate.data.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

inline fun LifecycleOwner.postDelayed(
    crossinline body: suspend CoroutineScope.() -> Unit,
    delay: Long
) {
    lifecycleScope.launchWhenStarted {
        delay(delay)
        body.invoke(this)
    }
}

inline fun <T> LifecycleOwner.observe(liveData: LiveData<T>, crossinline observer: (T) -> Unit) {
    liveData.observe(this) {
        try {
            it?.let(observer)
        } catch (e: Exception) {
            Logger.print("Observe exception: ${e.message}")
        }
    }
}

fun <T> Fragment.setNavigationResult(key: NavigationResultKey, value: T) {
    this.findNavController().previousBackStackEntry?.savedStateHandle?.set(key.name, value)
}

fun <T> Fragment.getNavigationResult(key: NavigationResultKey, onResult: (result: T) -> Unit) {
    val navBackStackEntry = findNavController().currentBackStackEntry

    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME
            && navBackStackEntry?.savedStateHandle?.contains(key.name) == true
        ) {
            val result = navBackStackEntry.savedStateHandle.get<T>(key.name)
            result?.let(onResult)
            navBackStackEntry.savedStateHandle.remove<T>(key.name)
        }
    }
    navBackStackEntry?.lifecycle?.addObserver(observer)

    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            navBackStackEntry?.lifecycle?.removeObserver(observer)
        }
    })
}

enum class NavigationResultKey {
    RELOAD, SAVE_POSITION, USER, CHECK_GENDER
}