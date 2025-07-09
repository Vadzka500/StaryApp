package com.example.navwithapinothing_2.features.screen.FolderScreen

import androidx.compose.ui.graphics.Color
import com.example.navwithapinothing_2.database.models.Folder
import com.example.navwithapinothing_2.database.models.FolderWithMovies
import com.example.navwithapinothing_2.models.common.ListMoviesResult
import com.example.navwithapinothing_2.models.common.SortDirection
import com.example.navwithapinothing_2.models.common.SortType
import com.example.navwithapinothing_2.models.common.ViewMode
import com.example.navwithapinothing_2.utils.FiltersUtil

/**
 * @Author: Vadim
 * @Date: 03.07.2025
 */

data class FolderState(
    val list: ListMoviesResult = ListMoviesResult.Loading,
    val countMovies: Int = 0,
    val folder: FolderWithMovies? = null,
    val isShowDialog: Boolean = false,
    val viewMode: ViewMode = ViewMode.GRID,
    val isShowFilter: Boolean = false,
    val sortType: SortType = SortType.NONE,
    val sortDirection: SortDirection = SortDirection.DESCENDING
)