package com.example.navwithapinothing_2.features.screen.MoviesListScreen

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */
sealed interface MainIntent {
    data class OnSelectMovie(val id: Long): MainIntent
    data class OnSelectCollection(val name: String, val slug: String): MainIntent
    data object OsSelectCollections: MainIntent
    data object ToErrorScreen: MainIntent
}