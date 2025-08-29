package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    val await: Double? = null,
    val filmCritics: Double? = null,
    val imdb: Double,
    val kp: Double,
    val russianFilmCritics: Double? = null,
    val tmdb: Double? = null
)
