package com.example.navwithapinothing_2.navigation

import com.example.moviesapi.models.movie.MovieDTO
import kotlinx.serialization.Serializable
import kotlin.collections.List

/**
 * @Author: Vadim
 * @Date: 23.04.2025
 */
@Serializable
data class ListMovies(val label: String, val slug: String)