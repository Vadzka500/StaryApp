package com.sidspace.stary.collectionmovies.presentation.screen


sealed class ListMoviesEffect {
    data class OnSelectMovie(val id: Long): ListMoviesEffect()
    object OnBack: ListMoviesEffect()
    object ToErrorScreen: ListMoviesEffect()
}