package com.example.navwithapinothing_2.features.screen.ReviewScreen

/**
 * @Author: Vadim
 * @Date: 03.07.2025
 */
sealed interface ReviewIntent {
    data class LoadReviews(val id: Long): ReviewIntent
    object OnBack: ReviewIntent
}