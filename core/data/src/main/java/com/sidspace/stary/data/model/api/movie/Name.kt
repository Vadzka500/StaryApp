package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val language: String? = null,
    val name: String,
    val type: String? = null
)
