package com.sidspace.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val name: String? = null
)