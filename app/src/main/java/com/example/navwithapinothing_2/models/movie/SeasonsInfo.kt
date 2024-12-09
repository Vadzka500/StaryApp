package com.example.moviesapi.models.movie

import kotlinx.serialization.Serializable

@Serializable
data class SeasonsInfo(
    val episodesCount: Int,
    val number: Int
)