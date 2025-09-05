package com.sidspace.stary.folder.presentation.screen

import com.sidspace.stary.ui.enums.SortType


sealed interface FolderIntent {

    data class LoadFolder(val id: Long) : FolderIntent
    data class OnSelectMovie(val id: Long) : FolderIntent
    data object OnBack : FolderIntent
    data object ShowDialog : FolderIntent
    data object HideDialog : FolderIntent
    data object RemoveFolder : FolderIntent

    data class IsShowFilters(val isShow: Boolean) : FolderIntent
    data object SetGridView : FolderIntent
    data object SetListView : FolderIntent
    object ToggleSortDirection : FolderIntent
    data class SetSortType(val sort: SortType) : FolderIntent
    object SortMovies : FolderIntent
    object OnError : FolderIntent
}
