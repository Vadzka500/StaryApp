package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class Audience(
    val count: Int,
    val country: String
)