package com.sidspace.stary.person.data.repository


import com.sidspace.stary.data.api.MovieApi
import com.sidspace.stary.data.mapper.toDomain
import com.sidspace.stary.data.mapper.toMovie
import com.sidspace.stary.data.mapper.toPerson
import com.sidspace.stary.data.utils.mapSuccess
import com.sidspace.stary.data.utils.safeCall
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Person
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.person.domain.repository.PersonRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(private val movieApi: MovieApi) : PersonRepository {
    override fun getMovieByPerson(personId: Long): Flow<Result<List<Movie>>> = flow {
        emit(safeCall { movieApi.getMovieByPerson(personId) }.mapSuccess { it.docs }
            .toDomain { it.map { it.toMovie() } })
    }

    override fun getPersonById(personId: Long): Flow<Result<Person>> = flow {
        emit(safeCall { movieApi.getPersonById(personId) }.toDomain { it.toPerson() })
    }
}
