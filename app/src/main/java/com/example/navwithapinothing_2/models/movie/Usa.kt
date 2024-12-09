package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class Usa(
    val currency: String? = null,
    val value: Int? = null
)