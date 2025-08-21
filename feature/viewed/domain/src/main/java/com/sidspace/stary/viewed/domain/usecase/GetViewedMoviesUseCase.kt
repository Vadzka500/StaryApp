package com.sidspace.stary.viewed.domain.usecase


import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.viewed.domain.repository.ViewedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetViewedMoviesUseCase @Inject constructor(private val repository: ViewedRepository) {
    suspend operator fun invoke(): Flow<Result<List<Movie>>> = repository.getViewedMovies()
}