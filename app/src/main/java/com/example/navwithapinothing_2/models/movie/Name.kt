package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val language: String? = null,
    val name: String,
    val type: String? = null
)