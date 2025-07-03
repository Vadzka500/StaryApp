package com.example.navwithapinothing_2.features.screen.ReviewScreen

import com.example.navwithapinothing_2.models.UserReview

/**
 * @Author: Vadim
 * @Date: 03.07.2025
 */

data class ReviewState(
    val reviews: ReviewResult = ReviewResult.Loading,
    val countReviews: Int = 0,
)

sealed class ReviewResult{
    object Loading: ReviewResult()
    data class Error(val message: String): ReviewResult()
    data class Success(val list: List<UserReview>): ReviewResult()
}