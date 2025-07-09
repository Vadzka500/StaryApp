package com.example.navwithapinothing_2.features.screen.CollectionMoviesScreen

import com.example.navwithapinothing_2.features.screen.SearchScreen.SearchIntent
import com.example.navwithapinothing_2.models.common.SortType

/**
 * @Author: Vadim
 * @Date: 08.07.2025
 */
sealed interface ListMoviesIntent {
    data class OnSelectMovie(val id: Long): ListMoviesIntent
    data object OnBack: ListMoviesIntent
    data class IsShowFilter(val isShow: Boolean): ListMoviesIntent
    object SetGridViewMode: ListMoviesIntent
    object SetListViewMode: ListMoviesIntent
    object ToggleSortDirection: ListMoviesIntent
    data class SetSortType(val sort: SortType): ListMoviesIntent
    object SortMovies: ListMoviesIntent
}