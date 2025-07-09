package com.example.navwithapinothing_2.features.screen.AccountScreen

import com.example.moviesapi.models.movie.MovieDTO


/**
 * @Author: Vadim
 * @Date: 26.06.2025
 */
data class AccountState (
    val resultAccountViewed : ResultAccountData = ResultAccountData.None,
    val resultAccountBookmark : ResultAccountData = ResultAccountData.None,
    val countViewed: Int = 0,
    val countBookmark: Int = 0,
    val countFolders: Int = 0,
    val isShowEmptyHint: Boolean = false,
    val bookmarkScrollOffSet:Int = 0,
    val scrollViewed: ScrollState = ScrollState(),
    val scrollBookmark: ScrollState = ScrollState()
)

data class ScrollState(
    val scrollIndex: Int = 0,
    val scrollOffSet: Int = 0
)

sealed class ResultAccountData(){
    object None: ResultAccountData()
    object Loading: ResultAccountData()
    data class Error(val message: String): ResultAccountData()
    data class Success(val data: List<MovieDTO>): ResultAccountData()
}

