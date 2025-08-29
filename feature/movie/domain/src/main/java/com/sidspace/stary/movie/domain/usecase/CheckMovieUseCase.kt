package com.sidspace.stary.movie.domain.usecase

import com.sidspace.stary.domain.model.LocalMovie
import com.sidspace.stary.domain.model.LocalResult
import com.sidspace.stary.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckMovieUseCase @Inject constructor(private val movieDatabaseRepository: MovieRepository) {

    operator fun invoke(id: Long): Flow<LocalResult<LocalMovie?>> =
        movieDatabaseRepository.existMovie(id)
}
