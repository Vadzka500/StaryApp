package com.sidspace.stary.bookmark.domain.repository

import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {

    fun getBookmarkFromDb(): Flow<Result<List<Movie>>>

    fun getBookmarkMovies(list: List<Movie>): Flow<Result<List<Movie>>>
}