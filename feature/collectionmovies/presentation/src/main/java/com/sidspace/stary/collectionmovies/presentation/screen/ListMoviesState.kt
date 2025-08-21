package com.sidspace.stary.collectionmovies.presentation.screen

import com.example.ui.enum.SortDirection
import com.example.ui.enum.SortType
import com.example.ui.enum.ViewMode
import com.example.ui.model.MovieData
import com.example.ui.model.ResultData


data class ListMoviesState(
    val list: ResultData<List<MovieData>> = ResultData.Loading,
    val viewMode: ViewMode = ViewMode.GRID,
    val isShowFilter: Boolean = false,
    val sortType: SortType = SortType.NONE,
    val sortDirection: SortDirection = SortDirection.DESCENDING
)







