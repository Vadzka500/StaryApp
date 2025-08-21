package com.sidspace.stary.data.utils

import retrofit2.Response


suspend fun <T> safeCall(fund: suspend () -> Response<T>): ResultRemote<T> {
    return try {
        val response = fund()

        if (response.isSuccessful) {
            val data = response.body()
            if (data != null)
                ResultRemote.Success(data)
            else
                ResultRemote.Error("")
        } else {
            ResultRemote.Error(response.code())
        }
    } catch (e: Exception) {

        e.printStackTrace()
        ResultRemote.Error(e.message)
    }
}

inline fun <T, R> ResultRemote<T>.mapSuccess(transform: (T) -> R): ResultRemote<R> {
    return when (this) {
        is ResultRemote.Error<*> -> this
        is ResultRemote.Loading -> this
        is ResultRemote.Success<T> -> ResultRemote.Success(transform(data))
    }
}

sealed class ResultRemote<out T> {
    data object Loading : ResultRemote<Nothing>()
    data class Success<T>(val data: T) : ResultRemote<T>()
    data class Error<String>(val data: String) : ResultRemote<Nothing>()
}