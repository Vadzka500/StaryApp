package com.sidspace.stary.collectionmovies.domain.usecase

import com.sidspace.stary.collectionmovies.domain.repository.CollectionMoviesRepository
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetListMoviesByCollectionUseCase @Inject constructor(private val repository: CollectionMoviesRepository) {
    suspend operator fun invoke(slug: String, limit: Int): Flow<Result<List<Movie>>> {
        return repository.getMoviesByCollection(slug, limit)
    }
}
