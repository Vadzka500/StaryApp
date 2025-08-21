package com.example.domain.usecase.person


import com.sidspace.stary.domain.model.Person
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.person.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject


class GetPersonByIdUseCase @Inject constructor(private val repository: PersonRepository) {
    operator fun invoke(id:Long): Flow<Result<Person>> = repository.getPersonById(id)
}