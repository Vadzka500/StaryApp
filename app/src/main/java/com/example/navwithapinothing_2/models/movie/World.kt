package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class World(
    val currency: String? = null,
    val value: Long? = null
)