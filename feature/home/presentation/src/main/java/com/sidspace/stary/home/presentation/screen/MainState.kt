package com.sidspace.stary.home.presentation.screen

import com.example.ui.model.CollectionUi
import com.example.ui.model.MovieUi
import com.example.ui.model.ResultData



data class MainState(
    val listTopBanned: ResultData<List<MovieUi>> = ResultData.Loading,
    val listHomePage: ResultData<Map<Pair<String, String>, ResultData<List<MovieUi>>?>> = ResultData.Loading,
    val listCollection: ResultData<List<CollectionUi>> = ResultData.Loading
)
