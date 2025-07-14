package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class ReleaseYear(
    val end: Int? = null,
    val start: Int? = null
)