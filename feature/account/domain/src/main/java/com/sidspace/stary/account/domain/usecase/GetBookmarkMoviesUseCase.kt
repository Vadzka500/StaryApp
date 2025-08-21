package com.example.domain.usecase.movie


import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result

import com.sidspace.stary.account.domain.repository.AccountRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetBookmarkMoviesUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(): Flow<Result<List<Movie>>> = repository.getBookmarkMovies()
}