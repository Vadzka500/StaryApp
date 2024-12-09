package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class ItemX(
    val logo: Logo,
    val name: String,
    val url: String
)