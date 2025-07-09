package com.example.navwithapinothing_2.features.screen.BookmarkMoviesScreen

import com.example.navwithapinothing_2.features.screen.ViewedMoviesScreen.ViewedMovieIntent
import com.example.navwithapinothing_2.models.common.SortType

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */
sealed interface BookmarkMoviesIntent {
    data object OnBack: BookmarkMoviesIntent
    data class OnSelectMovie(val id:Long): BookmarkMoviesIntent
    data class IsShowFilters(val isShow:Boolean): BookmarkMoviesIntent
    data object SetGridView: BookmarkMoviesIntent
    data object SetListView: BookmarkMoviesIntent
    object ToggleSortDirection: BookmarkMoviesIntent
    data class SetSortType(val sort: SortType): BookmarkMoviesIntent
    object SortMovies: BookmarkMoviesIntent
}