package com.example.moviesapi.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    @SerialName("total") val total1: Int,
    @SerialName("limit") val limit1: Int,
    @SerialName("page") val page: Int,
    @SerialName("pages") val pages: Int,
    @SerialName("docs") val data: List<T>
)