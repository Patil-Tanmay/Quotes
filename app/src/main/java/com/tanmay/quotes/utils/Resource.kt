package com.tanmay.quotes.utils

sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    data class Success<T>(val dataFetched: T) : Resource<T>(dataFetched)
    class Loading<T> : Resource<T>()
    data class Error<T>(val e: Throwable): Resource<T>(null, e)

}
