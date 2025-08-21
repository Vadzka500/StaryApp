package com.sidspace.stary.search.presentation.screen

import com.example.ui.enum.ViewMode
import com.example.ui.model.MovieData
import com.example.ui.model.ResultData



data class SearchState(
    val list: ResultData<List<MovieData>> = ResultData.Loading,
    val searchStr: String = "",
    val viewMode: ViewMode = ViewMode.GRID,
    val isVisibleFilter: Boolean = false
)
