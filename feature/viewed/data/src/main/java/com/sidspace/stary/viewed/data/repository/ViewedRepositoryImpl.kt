package com.sidspace.stary.viewed.data.repository

import com.sidspace.stary.core.data.api.MovieApi
import com.sidspace.stary.core.data.database.MovieDao

import com.sidspace.stary.core.data.utils.mapSuccess
import com.sidspace.stary.core.data.utils.safeCall
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.viewed.domain.repository.ViewedRepository
import com.sidspace.stary.core.data.mapper.toDomain
import com.sidspace.stary.core.data.mapper.toMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class ViewedRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDao
): ViewedRepository  {
    override suspend fun getViewedMovies(): Flow<Result<List<Movie>>>  = flow{
        emit(Result.Loading)

        //val sorted = list.mapNotNull { id -> (result.data as List<MovieUi>).find { item -> item.id == id } }

        movieDatabase.getViewedMovies().collect { result ->

            /*val list = safeCall { movieApi.getMoviesByIds(result.map { it.movieId }) }.mapSuccess { it.docs }
                .toDomain( { it.map { it.toMovie() }} )*/


            emit(safeCall { movieApi.getMoviesByIds(result.map { it.movieId }) }.mapSuccess { it.docs }
                .toDomain { it.map { it.toMovie() } })


        }
    }
}