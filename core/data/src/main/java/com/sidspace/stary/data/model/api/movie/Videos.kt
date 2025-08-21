package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class Videos(
    val trailers: List<Trailer>? = null
)