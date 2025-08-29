package com.sidspace.stary.random.domain.usecase


import com.sidspace.stary.domain.RandomFiltersOption
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.random.domain.repository.RandomRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetRandomMovieUseCase @Inject constructor(private val repository: RandomRepository) {
    suspend operator fun invoke(filter: RandomFiltersOption?): Flow<Result<Movie>> {
        return repository.getRandomMovie(filter = filter)
    }
}
