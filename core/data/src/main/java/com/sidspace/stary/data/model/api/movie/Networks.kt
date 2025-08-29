package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class Networks(
    val items: List<Item>? = null
)
