package com.sidspace.stary.movie.domain.usecase


import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.movie.domain.repository.MovieRepository
import javax.inject.Inject


class UpdateMovieViewedUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend operator fun invoke(id: Long, isViewed: Boolean): Result<Unit> = repository.updateMovieViewed(id, isViewed)
}