package com.example.navwithapinothing_2.usecase

import com.example.navwithapinothing_2.data.MovieDatabaseRepository

import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.MovieDb
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 04.06.2025
 */
class GetMoviesByCollection @Inject constructor(private val repository: MovieDatabaseRepository) {
    operator fun invoke(collection: String): Flow<ResultDb<List<MovieDb>>> = repository.getMoviesByCollection(collection)
}