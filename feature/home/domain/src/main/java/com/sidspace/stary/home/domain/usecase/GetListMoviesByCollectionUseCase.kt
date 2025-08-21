package com.sidspace.stary.home.domain.usecase

import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetListMoviesByCollectionUseCase @Inject constructor(private val repository: HomeRepository) {
    operator fun invoke(slug: String, limit: Int): Flow<Result<List<Movie>>> {
        return repository.getMoviesByCollection(slug, limit)
    }
}