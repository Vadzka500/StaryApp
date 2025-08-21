package com.sidspace.stary.collectionmovies.domain.repository

import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface CollectionMoviesRepository {
    suspend fun getMoviesByCollection(slug: String, limit: Int): Flow<Result<List<Movie>>>
}