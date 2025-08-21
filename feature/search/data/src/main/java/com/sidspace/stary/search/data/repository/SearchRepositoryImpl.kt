package com.sidspace.stary.search.data.repository

import com.sidspace.stary.data.api.MovieApi

import com.sidspace.stary.data.utils.mapSuccess
import com.sidspace.stary.data.utils.safeCall
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.search.domain.repository.SearchRepository
import com.sidspace.stary.data.mapper.toDomain
import com.sidspace.stary.data.mapper.toMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class SearchRepositoryImpl @Inject constructor(private val movieApi: MovieApi) : SearchRepository {
    override suspend fun getSearchedMovies(str: String): Flow<Result<List<Movie>>> = flow {
        emit(safeCall { movieApi.getAllMovies(search = str) }.mapSuccess { it.docs }
            .toDomain { it.map { it.toMovie() } })
    }
}