package com.example.recogidas_presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected open fun handleError(t: Throwable) {
        t.printStackTrace()
    }
    protected fun launchSafe(
        onLoading: ((Boolean) -> Unit)? = null,
        onError: ((String) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch {
            try {
                onLoading?.invoke(true)
                block()

            } catch (e: Exception) {
                val msg = e.message ?: "Unknown Error"
                onError?.invoke(msg)
                handleError(e)
            } finally {
                // Stop Loading
                onLoading?.invoke(false)
            }
        }
    }
}
