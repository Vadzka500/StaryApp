package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class Trailer(
    val name: String? = null,
    val site: String? = null,
    val size: Int? = null,
    val type: String? = null,
    val url: String? = null
)
