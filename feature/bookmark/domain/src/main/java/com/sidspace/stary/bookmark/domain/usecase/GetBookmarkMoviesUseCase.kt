package com.sidspace.stary.bookmark.domain.usecase

import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.bookmark.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarkMoviesUseCase @Inject constructor(private val repository: BookmarkRepository) {

    suspend operator fun invoke(): Flow<Result<List<Movie>>> = repository.getBookmarkMovies()
}