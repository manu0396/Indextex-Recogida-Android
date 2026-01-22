package com.example.core_common.dispatchers.result

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable) : Result<Nothing>
    object Loading : Result<Nothing>
}

fun <T> Result<T>.successOrNull(): T? = (this as? Result.Success)?.data
