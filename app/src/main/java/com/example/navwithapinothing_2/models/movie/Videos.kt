package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class Videos(
    val trailers: List<Trailer>? = null
)