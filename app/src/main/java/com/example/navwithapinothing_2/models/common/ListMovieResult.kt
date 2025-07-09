package com.example.navwithapinothing_2.models.common

import com.example.moviesapi.models.movie.MovieDTO

/**
 * @Author: Vadim
 * @Date: 08.07.2025
 */

sealed class ListMoviesResult {
    object Loading : ListMoviesResult()
    data class Error(val message: String) : ListMoviesResult()
    data class Success(val list: List<MovieDTO>) : ListMoviesResult()
}