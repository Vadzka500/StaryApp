package com.sidspace.stary.home.presentation.screen


sealed class MainEffect {

    data class OnSelectMovie(val id: Long) : MainEffect()
    data class OnSelectCollection(val name: String, val slug: String) : MainEffect()
    data object OsSelectCollections : MainEffect()
    data object ToErrorScreen : MainEffect()
}
