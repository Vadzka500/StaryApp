package com.sidspace.stary.ui.model

sealed class ResultData <out T>(){
    object None: ResultData<Nothing>()
    object Loading: ResultData<Nothing>()
    object Error: ResultData<Nothing>()
    data class Success<out T>(val data: T): ResultData<T>()
}