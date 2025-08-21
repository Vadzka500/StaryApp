package com.sidspace.stary.bookmark.presentation.screen

import com.example.ui.enum.SortType



sealed interface BookmarkMoviesIntent {
    data object OnBack: BookmarkMoviesIntent
    data class OnSelectMovie(val id:Long): BookmarkMoviesIntent
    data class IsShowFilters(val isShow:Boolean): BookmarkMoviesIntent
    data object SetGridView: BookmarkMoviesIntent
    data object SetListView: BookmarkMoviesIntent
    object ToggleSortDirection: BookmarkMoviesIntent
    data class SetSortType(val sort: SortType): BookmarkMoviesIntent
    object SortMovies: BookmarkMoviesIntent
    object ToErrorScreen: BookmarkMoviesIntent
}