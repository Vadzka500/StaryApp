package com.sidspace.stary.movie.domain.usecase

import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(id: Long): Flow<Result<Movie>> {
        return repository.getMovieById(id)
    }
}
