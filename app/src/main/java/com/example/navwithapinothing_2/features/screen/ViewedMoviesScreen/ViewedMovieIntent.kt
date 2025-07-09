package com.example.navwithapinothing_2.features.screen.ViewedMoviesScreen

import com.example.navwithapinothing_2.features.screen.CollectionMoviesScreen.ListMoviesIntent
import com.example.navwithapinothing_2.models.common.SortType

/**
 * @Author: Vadim
 * @Date: 08.07.2025
 */
sealed interface ViewedMovieIntent {

    data class OnSelectMovie(val id: Long): ViewedMovieIntent
    data object OnBack: ViewedMovieIntent
    data class IsShowFilters(val isShow:Boolean): ViewedMovieIntent
    data object SetGridView: ViewedMovieIntent
    data object SetListView: ViewedMovieIntent
    object ToggleSortDirection: ViewedMovieIntent
    data class SetSortType(val sort: SortType): ViewedMovieIntent
    object SortMovies: ViewedMovieIntent
}