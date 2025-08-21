package com.sidspace.stary.search.domain.repository

import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow


interface SearchRepository {
    suspend fun getSearchedMovies(str:String): Flow<Result<List<Movie>>>
}