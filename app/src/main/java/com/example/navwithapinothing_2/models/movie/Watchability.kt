package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class Watchability(
    val items: List<ItemX>
)