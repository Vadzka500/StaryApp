package com.example.navwithapinothing_2.usecase

import com.example.navwithapinothing_2.data.MovieDatabaseRepository
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.MovieDb
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 21.05.2025
 */
class RemoveMovieUseCase @Inject constructor(private val repository: MovieDatabaseRepository) {
    suspend operator fun invoke(id: Long): ResultDb<Unit> = repository.removeMovie(id)
}