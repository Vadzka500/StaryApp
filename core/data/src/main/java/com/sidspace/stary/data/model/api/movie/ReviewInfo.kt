package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class ReviewInfo(
    val count: Int,
    val percentage: String,
    val positiveCount: Int
)