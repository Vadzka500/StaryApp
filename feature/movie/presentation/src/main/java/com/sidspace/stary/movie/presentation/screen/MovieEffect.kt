package com.sidspace.stary.movie.presentation.screen


sealed class MovieEffect {

    data class ToMovieScreen(val id: Long) : MovieEffect()
    data class ToPersonScreen(val id: Long) : MovieEffect()
    data class ToReviewScreen(val id: Long) : MovieEffect()
    data class PlayTrailer(val url: String) : MovieEffect()
    object ToErrorScreen : MovieEffect()
}
