package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class Fact(
    val spoiler: Boolean,
    val type: String,
    val value: String
)