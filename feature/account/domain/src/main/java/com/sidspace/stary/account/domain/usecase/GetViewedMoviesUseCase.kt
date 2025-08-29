package com.sidspace.stary.account.domain.usecase

import com.sidspace.stary.account.domain.repository.AccountRepository
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetViewedMoviesUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(): Flow<Result<List<Movie>>> = repository.getViewedMovies()
}
