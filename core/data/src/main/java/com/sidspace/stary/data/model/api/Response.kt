package com.sidspace.stary.data.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    @SerialName("total") val total1: Int,
    @SerialName("limit") val limit1: Int,
    @SerialName("page") val page: Int,
    @SerialName("pages") val pages: Int,
    @SerialName("docs") val docs: List<T>
)