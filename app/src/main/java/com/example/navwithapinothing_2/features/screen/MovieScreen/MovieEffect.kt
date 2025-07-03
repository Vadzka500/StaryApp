package com.example.navwithapinothing_2.features.screen.MovieScreen

/**
 * @Author: Vadim
 * @Date: 28.06.2025
 */
sealed class MovieEffect {

    data class ToMovieScreen(val id: Long): MovieEffect()
    data class ToPersonScreen(val id: Long): MovieEffect()
    data class ToReviewScreen(val id: Long): MovieEffect()
    data class PlayTrailer(val url: String): MovieEffect()
}