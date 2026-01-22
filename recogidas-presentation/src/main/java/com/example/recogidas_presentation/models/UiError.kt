package com.example.recogidas_presentation.models

sealed class UiError {
    data class NetworkError(val message: String) : UiError()
    data class HardwareError(val code: Int) : UiError()
    data class UnknownError(val throwable: Throwable) : UiError()
}
