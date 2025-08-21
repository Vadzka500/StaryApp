package com.sidspace.stary.search.presentation.screen


import com.sidspace.stary.ui.enum.ViewMode
import com.sidspace.stary.ui.model.MovieData
import com.sidspace.stary.ui.model.ResultData


data class SearchState(
    val list: ResultData<List<MovieData>> = ResultData.Loading,
    val searchStr: String = "",
    val viewMode: ViewMode = ViewMode.GRID,
    val isVisibleFilter: Boolean = false
)
