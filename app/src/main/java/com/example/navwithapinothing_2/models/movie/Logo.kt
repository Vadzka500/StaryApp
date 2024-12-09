package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class Logo(
    val url: String? = null
)