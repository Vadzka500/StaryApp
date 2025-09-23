package com.sidspace.stary.home.presentation.screen


import com.sidspace.stary.ui.model.CollectionUi
import com.sidspace.stary.ui.model.MoviePreviewUi
import com.sidspace.stary.ui.model.ResultData


data class MainState(
    val listTopBanned: ResultData<List<MoviePreviewUi>> = ResultData.Loading,
    val listHomePage: ResultData<Map<Pair<String, String>, ResultData<List<MoviePreviewUi>>?>> = ResultData.Loading,
    val listCollection: ResultData<List<CollectionUi>> = ResultData.Loading
){
    companion object{
        const val TOP_BANNED_HORIZONTAL_PADDING = 90
        const val TOP_BANNED_TEXT_HEIGHT = 30
        const val MAX_MOVIES = 10
    }
}

