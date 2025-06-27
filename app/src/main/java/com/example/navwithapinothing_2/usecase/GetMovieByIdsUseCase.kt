package com.example.navwithapinothing_2.usecase

import com.example.navwithapinothing_2.data.MovieDatabaseRepository
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 27.06.2025
 */
class GetMovieByIdsUseCase @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(list: List<Long>): Flow<Result> = repository.getMoviesByIds(list)
}