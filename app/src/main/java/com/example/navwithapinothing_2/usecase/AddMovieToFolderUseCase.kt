package com.example.navwithapinothing_2.usecase

import com.example.navwithapinothing_2.data.MovieDatabaseRepository
import com.example.navwithapinothing_2.data.ResultDb
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 24.06.2025
 */
class AddMovieToFolderUseCase @Inject constructor(private val repository: MovieDatabaseRepository) {
    suspend operator fun invoke(idMovie: Long, idFolder:Long): ResultDb<Unit> = repository.addMovieToFolder(idMovie, idFolder)
}