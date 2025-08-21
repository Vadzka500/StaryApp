package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class Votes(
    val await: Int,
    val filmCritics: Int,
    val imdb: Int,
    val kp: Double,
    val russianFilmCritics: Int,
    val tmdb: Int? = null
)