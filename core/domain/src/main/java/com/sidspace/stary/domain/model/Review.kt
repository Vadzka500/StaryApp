package com.sidspace.stary.domain.model


data class Review(
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