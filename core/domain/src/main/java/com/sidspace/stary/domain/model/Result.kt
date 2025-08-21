package com.sidspace.stary.domain.model

sealed class Result<out T>{
    object Error: Result<Nothing>()
    object Loading: Result<Nothing>()
    data class Success<T>(val data: T): Result<T>()
}

sealed class LocalResult<out T>{
    object Error: LocalResult<Nothing>()
    object Loading: LocalResult<Nothing>()
    data class Success<T>(val data: T?): LocalResult<T>()
}
