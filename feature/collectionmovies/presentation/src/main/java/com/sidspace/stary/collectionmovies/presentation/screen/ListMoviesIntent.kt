package com.sidspace.stary.collectionmovies.presentation.screen

import com.sidspace.stary.ui.enums.SortType


sealed interface ListMoviesIntent {
    data class OnSelectMovie(val id: Long) : ListMoviesIntent
    data object OnBack : ListMoviesIntent
    data class IsShowFilter(val isShow: Boolean) : ListMoviesIntent
    object SetGridViewMode : ListMoviesIntent
    object SetListViewMode : ListMoviesIntent
    object ToggleSortDirection : ListMoviesIntent
    data class SetSortType(val sort: SortType) : ListMoviesIntent
    object SortMovies : ListMoviesIntent
    object ToErrorScreen : ListMoviesIntent
}
