package com.sidspace.stary.review.presentation.model


data class ReviewUi(
    val id: Long,
    val title: String,
    val type: String,
    val review: String,
    val date: String,
    val author: String,
    val reviewLikes: Long = 0,
    val reviewDislikes: Long = 0,
)
