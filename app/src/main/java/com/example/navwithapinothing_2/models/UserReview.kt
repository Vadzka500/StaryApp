package com.example.navwithapinothing_2.models

import kotlinx.serialization.Serializable

/**
 * @Author: Vadim
 * @Date: 11.06.2025
 */
@Serializable
data class UserReview(
    val id:Long,
    val movieId: Long,
    val title: String,
    val type: String,
    val review: String,
    val date: String,
    val author: String,
    val reviewLikes: Long = 0,
    val reviewDislikes: Long = 0,
)
