package com.example.navwithapinothing_2.usecase

import com.example.navwithapinothing_2.data.MovieDatabaseRepository

import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.MovieDb
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 21.05.2025
 */
class GetViewedMoviesUseCase @Inject constructor(private val repository: MovieDatabaseRepository) {
    operator fun invoke(): Flow<ResultDb<List<MovieDb>>> = repository.getViewedMovies()
}