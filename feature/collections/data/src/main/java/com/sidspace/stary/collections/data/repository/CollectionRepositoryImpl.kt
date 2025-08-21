package com.sidspace.stary.collections.data.repository

import com.sidspace.stary.core.data.api.MovieApi
import com.sidspace.stary.core.data.database.MovieDao

import com.sidspace.stary.core.data.utils.mapSuccess

import com.sidspace.stary.core.data.utils.safeCall
import com.sidspace.stary.domain.model.Collection
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.collections.domain.repository.CollectionRepository
import com.sidspace.stary.core.data.mapper.toCollection
import com.sidspace.stary.core.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class CollectionRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) : CollectionRepository {
    override fun getCollections(): Flow<Result<List<Collection>>> = flow {

        val result = safeCall { movieApi.getCollections() }.mapSuccess { it.docs }
            .toDomain {
                it.map {
                    it.toCollection().apply {
                        viewedCount = movieDao.getMovieByCollectionCount(slug).first().size
                    }
                }
            }
        emit(result)
    }
}