package com.example.core_common.exception

import java.io.IOException

sealed class AppExceptions(override val message: String, override val cause: Throwable? = null) : Exception(message, cause) {

    class NetworkException(cause: Throwable?) : AppExceptions("Network Error", cause)
    class SecurityViolation(msg: String) : AppExceptions(msg)
    class GenericException(cause: Throwable?) : AppExceptions("Unknown Error", cause)

    companion object {
        fun from(e: Throwable): AppExceptions = when(e) {
            is AppExceptions -> e
            is IOException -> NetworkException(e)
            else -> GenericException(e)
        }
    }
}
