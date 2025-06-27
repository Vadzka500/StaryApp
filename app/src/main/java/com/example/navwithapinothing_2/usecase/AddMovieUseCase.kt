package com.example.navwithapinothing_2.usecase

import com.example.navwithapinothing_2.data.MovieDatabaseRepository
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.CollectionMovieDb
import com.example.navwithapinothing_2.database.models.MovieDb
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 21.05.2025
 */
class AddMovieUseCase @Inject constructor(private val repository: MovieDatabaseRepository) {

    suspend operator fun invoke(movie: MovieDb, list: List<CollectionMovieDb>): ResultDb<Unit> {
        if (!repository.getMovieById(movie.movieId)) {
            repository.addMovie(movie = movie, list = list)

            return ResultDb.Success(Unit)
        } else return ResultDb.Error
    }

}