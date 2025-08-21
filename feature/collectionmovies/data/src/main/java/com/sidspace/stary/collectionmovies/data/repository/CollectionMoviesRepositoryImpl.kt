package com.sidspace.stary.collectionmovies.data.repository

import com.sidspace.stary.core.data.api.MovieApi

import com.sidspace.stary.core.data.utils.mapSuccess
import com.sidspace.stary.core.data.utils.safeCall
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.collectionmovies.domain.repository.CollectionMoviesRepository
import com.sidspace.stary.core.data.mapper.toDomain
import com.sidspace.stary.core.data.mapper.toMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class CollectionMoviesRepositoryImpl @Inject constructor(private val movieApi: MovieApi) :
    CollectionMoviesRepository {


    override suspend fun getMoviesByCollection(
        slug: String,
        limit: Int
    ): Flow<Result<List<Movie>>> = flow {
        emit(safeCall {
            movieApi.getMovieByCollection(
                list = slug,
                limit = limit
            )
        }.mapSuccess { it.docs }.toDomain { it.map { it.toMovie() } })
    }
}