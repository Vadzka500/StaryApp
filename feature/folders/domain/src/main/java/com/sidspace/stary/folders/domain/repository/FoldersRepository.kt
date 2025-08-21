package com.sidspace.stary.folders.domain.repository

import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface FoldersRepository {
    suspend fun addFolder(folder: Folder): Result<Unit>
    fun getFolders(): Flow<Result<List<Folder>>>
}