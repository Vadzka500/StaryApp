package com.sidspace.stary.viewed.presentation.screen

import com.sidspace.stary.ui.enum.SortDirection
import com.sidspace.stary.ui.enum.SortType
import com.sidspace.stary.ui.enum.ViewMode
import com.sidspace.stary.ui.model.MovieData
import com.sidspace.stary.ui.model.ResultData


data class ViewedMovieState(
    val list: ResultData<List<MovieData>> = ResultData.None,
    val countMovies: Int = 0,
    val viewMode: ViewMode = ViewMode.GRID,
    val isShowFilter: Boolean = false,
    val sortType: SortType = SortType.NONE,
    val sortDirection: SortDirection = SortDirection.DESCENDING
)

