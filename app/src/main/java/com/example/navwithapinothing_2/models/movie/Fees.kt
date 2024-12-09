package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class Fees(
    val russia: Russia? = null,
    val usa: Usa? = null,
    val world: World? = null
)