package com.sidspace.stary.viewed.presentation.screen

import com.sidspace.stary.ui.enum.SortType


sealed interface ViewedMovieIntent {

    data class OnSelectMovie(val id: Long): ViewedMovieIntent
    data object OnBack: ViewedMovieIntent
    data class IsShowFilters(val isShow:Boolean): ViewedMovieIntent
    data object SetGridView: ViewedMovieIntent
    data object SetListView: ViewedMovieIntent
    object ToggleSortDirection: ViewedMovieIntent
    data class SetSortType(val sort: SortType): ViewedMovieIntent
    object SortMovies: ViewedMovieIntent
    object OnError: ViewedMovieIntent
}