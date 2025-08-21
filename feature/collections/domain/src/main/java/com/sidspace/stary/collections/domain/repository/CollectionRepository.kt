package com.sidspace.stary.collections.domain.repository

import com.sidspace.stary.domain.model.Collection
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    fun getCollections(): Flow<Result<List<Collection>>>
}