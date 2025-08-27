package com.sidspace.stary.bookmark.domain.usecase

import com.sidspace.stary.bookmark.domain.repository.BookmarkRepository
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarkMoviesFromDbUseCase @Inject constructor(private val repository: BookmarkRepository) {
    operator fun invoke(): Flow<Result<List<Movie>>> = repository.getBookmarkFromDb()
}