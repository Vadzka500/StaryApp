package com.sidspace.stary.viewed.presentation.screen

import com.example.ui.enum.SortDirection
import com.example.ui.enum.SortType
import com.example.ui.enum.ViewMode
import com.example.ui.model.MovieData
import com.example.ui.model.MovieUi
import com.example.ui.model.ResultData



data class ViewedMovieState(
    val list: ResultData<List<MovieData>> = ResultData.None,
    val countMovies: Int = 0,
    val viewMode: ViewMode = ViewMode.GRID,
    val isShowFilter: Boolean = false,
    val sortType: SortType = SortType.NONE,
    val sortDirection: SortDirection = SortDirection.DESCENDING
)

