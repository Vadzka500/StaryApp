package com.example.domain.usecase.collection


import com.sidspace.stary.domain.model.Collection
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.random.domain.repository.RandomRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCollectionUseCase @Inject constructor(private val repository: RandomRepository) {
    suspend operator fun invoke(): Flow<Result<List<Collection>>> {
        return repository.getCollections()
    }
}