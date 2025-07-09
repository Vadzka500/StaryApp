package com.example.navwithapinothing_2.features.screen.PersonScreen

import com.example.moviesapi.models.movie.Person
import com.example.navwithapinothing_2.models.common.ListMoviesResult

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */
data class PersonState(
    val listOfMovie: ListMoviesResult = ListMoviesResult.Loading,
    val listOfSerials: ListMoviesResult = ListMoviesResult.Loading,
    val person: Person? = null
)
