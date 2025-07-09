package com.example.navwithapinothing_2.features.screen.FolderScreen

import com.example.navwithapinothing_2.features.screen.ViewedMoviesScreen.ViewedMovieIntent
import com.example.navwithapinothing_2.models.common.SortType

/**
 * @Author: Vadim
 * @Date: 03.07.2025
 */
sealed interface FolderIntent {

    data class LoadFolder(val id: Long): FolderIntent
    data class OnSelectMovie(val id: Long): FolderIntent
    data object OnBack: FolderIntent
    data object ShowDialog: FolderIntent
    data object HideDialog: FolderIntent
    data object RemoveFolder: FolderIntent

    data class IsShowFilters(val isShow:Boolean): FolderIntent
    data object SetGridView: FolderIntent
    data object SetListView: FolderIntent
    object ToggleSortDirection: FolderIntent
    data class SetSortType(val sort: SortType): FolderIntent
    object SortMovies: FolderIntent
}