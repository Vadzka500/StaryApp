package com.example.domain.usecase.movie


import com.sidspace.stary.domain.model.LocalMovie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.movie.domain.repository.MovieRepository

import javax.inject.Inject


class AddMovieUseCase @Inject constructor(private val repository: MovieRepository) {

    suspend operator fun invoke(movieId: LocalMovie, list: List<String>?): Result<Unit> {
        return repository.addMovie(movie = movieId, listOfCollection = list)
    }

}