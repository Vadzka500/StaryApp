package com.sidspace.stary.home.data.repository

import com.sidspace.stary.data.api.MovieApi
import com.sidspace.stary.data.database.MovieDao

import com.sidspace.stary.home.data.utils.FolderDefaults
import com.sidspace.stary.data.utils.mapSuccess
import com.sidspace.stary.data.utils.safeCall
import com.sidspace.stary.domain.model.Collection
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.home.domain.repository.HomeRepository
import com.sidspace.stary.data.mapper.toCollection
import com.sidspace.stary.data.mapper.toDomain
import com.sidspace.stary.data.mapper.toMovie
import com.sidspace.stary.domain.Logger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class HomeRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao,
    private val logger: Logger
) : HomeRepository {
    override fun getCollections(): Flow<Result<List<Collection>>> = flow {
        emit(safeCall { movieApi.getCollections() }.mapSuccess { it.docs }.toDomain {
            it.map { it.toCollection() }.sortedBy { it.name.contains("top") }
                .filter { it.name.length < 45 }
        })
    }

    override fun getMoviesByCollection(
        slug: String,
        limit: Int
    ): Flow<Result<List<Movie>>> = flow {
        emit(safeCall {
            movieApi.getMovieByCollection(
                list = slug,
                limit = limit
            )
        }.mapSuccess { it.docs }.toDomain { it.map { it -> it.toMovie() } })
    }

    override suspend fun initDefaultFolders() {

        movieDao.getCountFolders().collect { result ->
            if (result == 0L) {
                movieDao.addFolders(FolderDefaults.defaultFolders.reversed())
            }
        }

    }
}