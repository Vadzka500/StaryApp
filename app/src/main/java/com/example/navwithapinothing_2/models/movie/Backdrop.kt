package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class Backdrop(
    val previewUrl: String? = null,
    val url: String? = null
)