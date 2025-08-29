package com.sidspace.stary.data.model.api.review

import kotlinx.serialization.Serializable

@Serializable
data class UserReview(
    val id: Long,
    val movieId: Long,
    val title: String,
    val type: String,
    val review: String,
    val date: String,
    val author: String,
    val reviewLikes: Long = 0,
    val reviewDislikes: Long = 0,
)
