package com.example.navwithapinothing_2.features.screen.CollectionMoviesScreen

import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.models.common.ListMoviesResult
import com.example.navwithapinothing_2.models.common.SortDirection
import com.example.navwithapinothing_2.models.common.SortType
import com.example.navwithapinothing_2.models.common.ViewMode


/**
 * @Author: Vadim
 * @Date: 08.07.2025
 */
data class ListMoviesState(
    val list: ListMoviesResult = ListMoviesResult.Loading,
    val viewMode: ViewMode = ViewMode.GRID,
    val isShowFilter: Boolean = false,
    val sortType: SortType = SortType.NONE,
    val sortDirection: SortDirection = SortDirection.DESCENDING
)







