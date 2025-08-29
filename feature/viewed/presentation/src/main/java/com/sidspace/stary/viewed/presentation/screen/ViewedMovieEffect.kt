package com.sidspace.stary.viewed.presentation.screen


sealed class ViewedMovieEffect {
    data class OnSelectMovie(val id: Long) : ViewedMovieEffect()
    object OnBack : ViewedMovieEffect()
    object ToErrorScreen : ViewedMovieEffect()
}
