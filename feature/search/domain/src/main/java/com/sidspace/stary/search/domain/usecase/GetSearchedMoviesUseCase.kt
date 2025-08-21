package com.example.domain.usecase.movie

import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetSearchedMoviesUseCase @Inject constructor(private val repository: SearchRepository) {
    suspend operator fun invoke(str: String): Flow<Result<List<Movie>>> = repository.getSearchedMovies(str)
}