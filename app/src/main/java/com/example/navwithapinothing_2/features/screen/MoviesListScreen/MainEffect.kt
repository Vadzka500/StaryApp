package com.example.navwithapinothing_2.features.screen.MoviesListScreen

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */
sealed class MainEffect {

    data class OnSelectMovie(val id: Long): MainEffect()
    data class OnSelectCollection(val name: String, val slug: String): MainEffect()
    data object OsSelectCollections: MainEffect()
    data object ToErrorScreen: MainEffect()
}