package com.sidspace.stary.collectionmovies.presentation.screen


import com.sidspace.stary.ui.enums.SortDirection
import com.sidspace.stary.ui.enums.SortType
import com.sidspace.stary.ui.enums.ViewMode
import com.sidspace.stary.ui.model.MovieUi
import com.sidspace.stary.ui.model.ResultData


data class ListMoviesState(
    val list: ResultData<List<MovieUi>> = ResultData.Loading,
    val viewMode: ViewMode = ViewMode.GRID,
    val isShowFilter: Boolean = false,
    val sortType: SortType = SortType.NONE,
    val sortDirection: SortDirection = SortDirection.DESCENDING
){
    companion object{
        const val SHIMMER_ITEMS = 8
    }
}







