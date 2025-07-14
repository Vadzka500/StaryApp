package com.example.navwithapinothing_2.features.screen.RandomScreen

import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.features.screen.MoviesListScreen.ListCollectionResult
import com.example.navwithapinothing_2.utils.RandomFiltersOption

/**
 * @Author: Vadim
 * @Date: 10.07.2025
 */
data class RandomState(
    val isFiltersShown: Boolean = false,
    val isSearchButtonEnabled: Boolean = true,
    val filter: RandomFiltersOption = RandomFiltersOption(),
    val listOfCollections: ListCollectionResult = ListCollectionResult.Loading,
    val randomMovie: MovieRandomStatus = MovieRandomStatus.None,
    val initialPage:Int = 1,
    val currentPageOffSetFraction: Float = 0f,
    val isSearch: Boolean = false
)

sealed class MovieRandomStatus{
    object None: MovieRandomStatus()
    object Error: MovieRandomStatus()
    object Loading: MovieRandomStatus()
    data class Success(val movie: MovieDTO): MovieRandomStatus()
}
