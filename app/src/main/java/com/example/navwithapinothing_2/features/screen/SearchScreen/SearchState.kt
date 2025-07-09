package com.example.navwithapinothing_2.features.screen.SearchScreen

import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.models.UserReview
import com.example.navwithapinothing_2.models.common.ViewMode

/**
 * @Author: Vadim
 * @Date: 08.07.2025
 */

data class SearchState(
    val list: SearchResult = SearchResult.Loading,
    val searchStr: String = "",
    val viewMode: ViewMode = ViewMode.GRID,
    val isVisibleFilter: Boolean = false
)

sealed class SearchResult {
    object Loading : SearchResult()
    data class Error(val message: String) : SearchResult()
    data class Success(val list: List<MovieDTO>) : SearchResult()
}
