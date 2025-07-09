package com.example.navwithapinothing_2.features.screen.BookmarkMoviesScreen

import com.example.navwithapinothing_2.models.common.ListMoviesResult
import com.example.navwithapinothing_2.models.common.SortDirection
import com.example.navwithapinothing_2.models.common.SortType
import com.example.navwithapinothing_2.models.common.ViewMode

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */
data class BookmarkMoviesState(
    val list: ListMoviesResult = ListMoviesResult.Loading,
    val countMovies: Int = 0,
    val viewMode: ViewMode = ViewMode.GRID,
    val isShowFilter: Boolean = false,
    val sortType: SortType = SortType.NONE,
    val sortDirection: SortDirection = SortDirection.DESCENDING
)