package com.sidspace.stary.viewed.data.repository

import com.sidspace.stary.data.api.MovieApi
import com.sidspace.stary.data.database.MovieDao
import com.sidspace.stary.data.mapper.toDomain
import com.sidspace.stary.data.mapper.toMovie
import com.sidspace.stary.data.utils.mapSuccess
import com.sidspace.stary.data.utils.safeCall
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.viewed.domain.repository.ViewedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class ViewedRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDao
) : ViewedRepository {
    override fun getViewedMoviesFromDb(): Flow<Result<List<Movie>>> = flow {
        try {
            movieDatabase.getViewedMovies().collect {
                emit(Result.Success(it.map { item -> item.toMovie() }))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error)
        }
    }

    override fun getViewedMovies(list: List<Movie>): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)

        emit(safeCall { movieApi.getMoviesByIds(list.map { it.id }) }.mapSuccess { it.docs }
            .toDomain { listResult ->
                list.map { item ->
                    listResult.find { it.id == item.id }!!.toMovie()
                }
            })


    }
}
