package com.example.navwithapinothing_2.usecase

import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */
class GetPersonByIdUseCase @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(id:Long): Flow<Result> = repository.getPersonById(id)
}