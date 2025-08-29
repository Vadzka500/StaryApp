package com.sidspace.stary.home.domain.usecase

import com.sidspace.stary.domain.model.Collection
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<Result<List<Collection>>> {
        return repository.getCollections()
    }
}
