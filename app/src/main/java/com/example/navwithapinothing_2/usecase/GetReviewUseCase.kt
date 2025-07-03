package com.example.navwithapinothing_2.usecase

import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 03.07.2025
 */
class GetReviewUseCase @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(id: Long): Flow<Result>  = repository.getReviewsById(id)
}