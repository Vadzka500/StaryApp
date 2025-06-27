package com.example.navwithapinothing_2.features.screen.AccountScreen

import com.example.moviesapi.models.movie.MovieDTO


/**
 * @Author: Vadim
 * @Date: 26.06.2025
 */
data class AccountState (
    var resultAccountViewed : ResultAccountData = ResultAccountData.None,
    var resultAccountBookmark : ResultAccountData = ResultAccountData.None,
    var countViewed: Int = 0,
    var countBookmark: Int = 0,
    var isShowEmptyHint: Boolean = false
)

sealed class ResultAccountData(){
    object None: ResultAccountData()
    object Loading: ResultAccountData()
    data class Error(val message: String): ResultAccountData()
    data class Success(val data: List<MovieDTO>): ResultAccountData()
}

