package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class ExternalId(
    val imdb: String? = null,
    val kpHD: String? = null,
    val tmdb: Int? = null
)