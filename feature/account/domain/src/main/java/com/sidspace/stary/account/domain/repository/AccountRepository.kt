package com.sidspace.stary.account.domain.repository

import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow


interface AccountRepository {

    suspend fun getBookmarkMovies(): Flow<Result<List<Movie>>>

    suspend fun getViewedMovies(): Flow<Result<List<Movie>>>

    suspend fun getFoldersCount(): Flow<Result<Long>>
}