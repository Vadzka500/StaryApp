package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class SimilarMovy(
    val alternativeName: String? = null,
    val enName: String? = null,
    val id: Int? = null,
    val name: String? = null,
    val poster: Poster? = null,
    val rating: Rating? = null,
    val type: String? = null,
    val year: Int? = null
)