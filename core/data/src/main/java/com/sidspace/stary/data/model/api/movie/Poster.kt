package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class Poster(
    val previewUrl: String? = null,
    val url: String? = null
)
