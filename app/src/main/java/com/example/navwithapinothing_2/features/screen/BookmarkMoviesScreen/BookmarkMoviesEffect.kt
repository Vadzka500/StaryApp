package com.example.navwithapinothing_2.features.screen.BookmarkMoviesScreen

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */
sealed class BookmarkMoviesEffect {
    data object OnBack: BookmarkMoviesEffect()
    data class OnSelectMovie(val id: Long): BookmarkMoviesEffect()
}