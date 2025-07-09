package com.example.navwithapinothing_2.features.screen.ViewedMoviesScreen

/**
 * @Author: Vadim
 * @Date: 08.07.2025
 */
sealed class ViewedMovieEffect {
    data class OnSelectMovie(val id: Long): ViewedMovieEffect()
    object OnBack: ViewedMovieEffect()
}