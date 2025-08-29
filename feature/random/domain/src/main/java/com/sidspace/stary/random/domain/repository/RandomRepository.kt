package com.sidspace.stary.random.domain.repository

import com.sidspace.stary.domain.RandomFiltersOption
import com.sidspace.stary.domain.model.Collection
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow


interface RandomRepository {
    suspend fun getRandomMovie(filter: RandomFiltersOption?): Flow<Result<Movie>>
    suspend fun getCollections(): Flow<Result<List<Collection>>>
}
