package com.sidspace.stary.viewed.domain.repository

import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface ViewedRepository {
    fun getViewedMoviesFromDb(): Flow<Result<List<Movie>>>

    fun getViewedMovies(list: List<Movie>): Flow<Result<List<Movie>>>
}
