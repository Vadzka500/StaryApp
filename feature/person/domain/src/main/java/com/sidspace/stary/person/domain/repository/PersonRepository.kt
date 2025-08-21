package com.sidspace.stary.person.domain.repository

import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Person
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow


interface PersonRepository {

    fun getMovieByPerson(personId: Long): Flow<Result<List<Movie>>>

    fun getPersonById(personId: Long): Flow<Result<Person>>

}