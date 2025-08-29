package com.sidspace.stary.review.presentation.screen


sealed interface ReviewIntent {
    data class LoadReviews(val id: Long) : ReviewIntent
    object OnBack : ReviewIntent
    object OnError : ReviewIntent
}
