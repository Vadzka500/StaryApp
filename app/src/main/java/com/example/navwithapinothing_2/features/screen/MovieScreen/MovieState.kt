package com.example.navwithapinothing_2.features.screen.MovieScreen

import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.database.models.MovieDb
import com.example.navwithapinothing_2.features.screen.FoldersScreen.ResultFilterData

/**
 * @Author: Vadim
 * @Date: 28.06.2025
 */

data class MovieState(
    val movie: ResultMovie = ResultMovie.Loading,
    val isExistMovieDb: MovieDb? = null,
    val filters: ResultFilterData = ResultFilterData.Loading,
    val isShowSheetFolders: Boolean = false,
    val isShowTrailerSheet: Boolean = false
)

sealed class ResultMovie {
    object Loading : ResultMovie()
    data class Success(val movie: MovieDTO) : ResultMovie()
    data class Error(val message: String) : ResultMovie()
}