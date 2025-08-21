package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class ReleaseYear(
    val end: Int? = null,
    val start: Int? = null
)