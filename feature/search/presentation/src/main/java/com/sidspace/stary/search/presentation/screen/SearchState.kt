package com.sidspace.stary.search.presentation.screen


import com.sidspace.stary.ui.enums.ViewMode
import com.sidspace.stary.ui.model.MovieUi
import com.sidspace.stary.ui.model.ResultData


data class SearchState(
    val list: ResultData<List<MovieUi>> = ResultData.Loading,
    val searchStr: String = "",
    val viewMode: ViewMode = ViewMode.GRID,
    val isVisibleFilter: Boolean = false
)
