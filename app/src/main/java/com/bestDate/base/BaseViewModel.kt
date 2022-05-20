package com.bestDate.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestDate.data.model.HandleError
import com.bestDate.data.model.InternalException
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel: ViewModel() {
    val loadingEvent = LiveEvent<Boolean>()
    val errorLive = LiveEvent<HandleError>()

    fun doAsync(
        loading: Boolean = true,
        context: CoroutineContext = Dispatchers.IO,
        onError: ((Exception) -> Unit)? = null,
        request: suspend () -> Unit
    ) {
        viewModelScope.launch(context) {
            try {
                if (loading) loadingEvent.postValue(true)
                request()
            } catch (e: Exception) {
                val exception = if (e !is InternalException) InternalException.UnknownException(e) else e
                onError?.invoke(exception)
                errorLive.postValue(HandleError(exception))
            } finally {
                if (loading) loadingEvent.postValue(false)
            }
        }
    }
}