package com.sidspace.stary.account.data.repository

import com.sidspace.stary.data.api.MovieApi
import com.sidspace.stary.data.database.MovieDao

import com.sidspace.stary.data.utils.mapSuccess
import com.sidspace.stary.data.utils.safeCall
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.account.domain.repository.AccountRepository
import com.sidspace.stary.data.mapper.toDomain
import com.sidspace.stary.data.mapper.toMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class AccountRepositoryImpl @Inject constructor(
    private val movieApiService: MovieApi,
    private val movieDatabase: MovieDao
) : AccountRepository {
    override suspend fun getBookmarkMovies(): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)

        movieDatabase.getBookmarkMovies().collect {

            emit(safeCall { movieApiService.getMoviesByIds(it.map { it.movieId }) }.mapSuccess { it.docs }
                .toDomain { it -> it.map { it.toMovie() } })

        }
    }

    override suspend fun getViewedMovies(): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)

        movieDatabase.getViewedMovies().collect {
            emit(safeCall { movieApiService.getMoviesByIds(it.map { it.movieId }) }.mapSuccess { it.docs }
                .toDomain { it.map { it.toMovie() } })
        }
    }

    override suspend fun getFoldersCount(): Flow<Result<Long>>  = flow {
        emit(Result.Loading)

        movieDatabase.getCountFolders().collect {
            emit(Result.Success(it))
        }
    }
}

