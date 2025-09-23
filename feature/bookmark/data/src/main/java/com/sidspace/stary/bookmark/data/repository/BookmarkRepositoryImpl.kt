package com.sidspace.stary.bookmark.data.repository

import com.sidspace.stary.bookmark.domain.repository.BookmarkRepository
import com.sidspace.stary.data.api.MovieApi
import com.sidspace.stary.data.database.MovieDao
import com.sidspace.stary.data.mapper.toDomain
import com.sidspace.stary.data.mapper.toMovie
import com.sidspace.stary.data.utils.mapSuccess
import com.sidspace.stary.data.utils.safeCall
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDao
) : BookmarkRepository {
    override fun getBookmarkFromDb(): Flow<Result<List<Movie>>> =

        movieDatabase.getBookmarkMovies().map { list ->
            Result.Success(list.map { it.toMovie() })

        }.catch { error ->
            error.printStackTrace()
            Result.Error
        }.onStart {
            Result.Loading
        }


    override fun getBookmarkMovies(list: List<Movie>): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)

        emit(
            safeCall { movieApi.getMoviesByIds(list.map { it.id }) }.mapSuccess { it.docs }
                .toDomain { listResult ->
                    list.map { item ->
                        listResult.find { it.id == item.id }!!.toMovie()
                    }
                }
        )

    }

}
