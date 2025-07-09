package com.example.navwithapinothing_2.features.screen.MoviesListScreen

import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.models.collection.CollectionMovie
import com.example.navwithapinothing_2.models.common.ListMoviesResult

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */
data class MainState(
    val listTopBanned: ListMoviesResult = ListMoviesResult.Loading,
    val listHomePage: ListHomePageResult = ListHomePageResult.Loading,
    val listCollection: ListCollectionResult = ListCollectionResult.Loading
)

sealed class ListCollectionResult{
    data object Loading: ListCollectionResult()
    data object Error: ListCollectionResult()
    data class Success(val data: List<CollectionMovie>): ListCollectionResult()
}
sealed class ListHomePageResult{
    object Loading: ListHomePageResult()
    object Error: ListHomePageResult()
    data class Success(val data: Map<Pair<String, String>, Result?>): ListHomePageResult()
}