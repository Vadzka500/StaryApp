package com.example.navwithapinothing_2.usecase

import com.example.navwithapinothing_2.data.MovieDatabaseRepository
import com.example.navwithapinothing_2.data.ResultDb
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 23.06.2025
 */
class GetFoldersCountUseCase @Inject constructor(private val repository: MovieDatabaseRepository) {

    suspend operator fun invoke(): Long = repository.getFoldersCount()
}