package com.sidspace.stary.bookmark.data.repository

import com.sidspace.stary.core.data.api.MovieApi
import com.sidspace.stary.core.data.database.MovieDao

import com.sidspace.stary.core.data.utils.mapSuccess
import com.sidspace.stary.core.data.utils.safeCall
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.core.data.mapper.toDomain
import com.sidspace.stary.core.data.mapper.toMovie
import com.sidspace.stary.bookmark.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class BookmarkRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDao
) : BookmarkRepository {
    override suspend fun getBookmarkMovies(): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)

        //val sorted = list.mapNotNull { id -> (result.data as List<MovieUi>).find { item -> item.id == id } }

        movieDatabase.getBookmarkMovies().collect { result ->

            /*val list = safeCall { movieApi.getMoviesByIds(result.map { it.movieId }) }.mapSuccess { it.docs }
                .toDomain( { it.map { it.toMovie() }} )*/


            emit(safeCall { movieApi.getMoviesByIds(result.map { it.movieId }) }.mapSuccess { it.docs }
                .toDomain { it.map { it.toMovie() } })


        }

    }

}
