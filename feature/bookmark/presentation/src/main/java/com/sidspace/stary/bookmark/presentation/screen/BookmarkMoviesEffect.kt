package com.sidspace.stary.bookmark.presentation.screen


sealed class BookmarkMoviesEffect {
    data object OnBack: BookmarkMoviesEffect()
    data class OnSelectMovie(val id: Long): BookmarkMoviesEffect()
    object ToErrorScreen: BookmarkMoviesEffect()
}