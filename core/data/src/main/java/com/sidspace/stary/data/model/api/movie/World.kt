package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class World(
    val currency: String? = null,
    val value: Long? = null
)