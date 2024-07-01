package com.hrithikvish.mvvmpostsapp.util

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null,
    val operation: Operation? = null
) {
    class Success<T>(
        data: T,
        operation: Operation? = null
    ): NetworkResult<T>(
        data = data,
        operation = operation
    )

    class Error<T>(
        message: String?,
        data: T? = null
    ): NetworkResult<T>(
        data,
        message
    )

    class Loading<T>: NetworkResult<T>()
}

enum class Operation {
    UPDATE_POST, DELETE_POST
}