package com.sidspace.stary.home.presentation.screen


sealed interface MainIntent {
    data class OnSelectMovie(val id: Long): MainIntent
    data class OnSelectCollection(val name: String, val slug: String): MainIntent
    data object OsSelectCollections: MainIntent
    data object ToErrorScreen: MainIntent
}