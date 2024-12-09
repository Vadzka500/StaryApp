package com.example.moviesapi.models.movie

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    val await: Double? = null,
    val filmCritics: Double,
    val imdb: Double,
    val kp: Double,
    val russianFilmCritics: Double,
    val tmdb: Double? = null
)