package com.example.domain.usecase.movie

import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.person.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetMoviesByPersonIdUseCase @Inject constructor(private val repository: PersonRepository) {
    operator fun invoke(id: Long) : Flow<Result<List<Movie>>> = repository.getMovieByPerson(id)
}