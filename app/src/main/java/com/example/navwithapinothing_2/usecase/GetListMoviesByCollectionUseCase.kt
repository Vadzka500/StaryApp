package com.example.navwithapinothing_2.usecase

import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 08.07.2025
 */
class GetListMoviesByCollectionUseCase @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(slug: String, limit: Int) : Flow<Result> = repository.getListMovieByCollection(slug, limit)
}