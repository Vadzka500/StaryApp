package com.sidspace.stary.collections.domain.usecase


import com.sidspace.stary.collections.domain.repository.CollectionRepository
import com.sidspace.stary.domain.model.Collection
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCollectionUseCase @Inject constructor(private val repository: CollectionRepository) {
    operator fun invoke(): Flow<Result<List<Collection>>> {
        return repository.getCollections()
    }
}
