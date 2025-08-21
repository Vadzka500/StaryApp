package com.sidspace.stary.bookmark.presentation.screen

import com.example.ui.enum.SortDirection
import com.example.ui.model.ResultData
import com.example.ui.enum.SortType
import com.example.ui.enum.ViewMode
import com.example.ui.model.MovieData


data class BookmarkMoviesState(
    val list: ResultData<List<MovieData>> = ResultData.None,
    val countMovies: Int = 0,
    val viewMode: ViewMode = ViewMode.GRID,
    val isShowFilter: Boolean = false,
    val sortType: SortType = SortType.NONE,
    val sortDirection: SortDirection = SortDirection.DESCENDING
)
