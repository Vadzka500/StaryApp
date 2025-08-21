package com.sidspace.stary.bookmark.domain.repository

import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {

    suspend fun getBookmarkMovies(): Flow<Result<List<Movie>>>
}