package com.sidspace.stary.home.domain.repository

import com.sidspace.stary.domain.model.Collection
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow


interface HomeRepository {

    fun getCollections(): Flow<Result<List<Collection>>>

    fun getMoviesByCollection(slug: String, limit: Int): Flow<Result<List<Movie>>>

    suspend fun initDefaultFolders()
}