package com.example.core_common.exception

sealed class RecogidaException : Throwable() {
    object NetworkError : RecogidaException()
    object ScanFailedException : RecogidaException()
    data class Unknown(override val message: String) : RecogidaException()
}
