package com.sidspace.stary.home.domain.usecase


import com.sidspace.stary.domain.Logger
import com.sidspace.stary.domain.model.Collection
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCollectionUseCase @Inject constructor(private val repository: HomeRepository, private val logger: Logger) {
    operator fun invoke(): Flow<Result<List<Collection>>> {
        return repository.getCollections()
    }
}