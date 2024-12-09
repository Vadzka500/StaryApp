package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class Networks(
    val items: List<Item>? = null
)