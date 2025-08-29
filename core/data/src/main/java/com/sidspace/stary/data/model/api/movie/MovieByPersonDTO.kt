package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class MovieByPersonDTO(
    val id: Long,
    val name: String? = null,
    val rating: Double? = null,
    val description: String? = null,
)
