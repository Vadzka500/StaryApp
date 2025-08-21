package com.sidspace.stary.search.presentation.screen


sealed class SearchEffect {
    data class OnSelectMovie(val id: Long): SearchEffect()
    object ToErrorScreen: SearchEffect()
}