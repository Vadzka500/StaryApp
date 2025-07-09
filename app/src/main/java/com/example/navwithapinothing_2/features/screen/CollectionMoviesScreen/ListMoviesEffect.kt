package com.example.navwithapinothing_2.features.screen.CollectionMoviesScreen

/**
 * @Author: Vadim
 * @Date: 08.07.2025
 */
sealed class ListMoviesEffect {
    data class OnSelectMovie(val id: Long): ListMoviesEffect()
    object OnBack: ListMoviesEffect()
}