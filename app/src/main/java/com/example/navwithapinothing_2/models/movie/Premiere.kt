package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class Premiere(
    val bluray: String? = null,
    val cinema: String? = null,
    val country: String? = null,
    val digital: String? = null,
    val dvd: String? = null,
    val russia: String? = null,
    val world: String? = null
)