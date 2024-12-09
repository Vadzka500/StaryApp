package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val logo: Logo? = null,
    val name: String? = null
)