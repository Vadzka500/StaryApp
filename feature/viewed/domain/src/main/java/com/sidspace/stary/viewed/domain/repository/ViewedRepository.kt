package com.sidspace.stary.viewed.domain.repository

import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow


interface ViewedRepository {

    suspend fun getViewedMovies(): Flow<Result<List<Movie>>>
}