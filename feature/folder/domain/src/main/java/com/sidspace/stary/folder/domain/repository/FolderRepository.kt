package com.sidspace.stary.folder.domain.repository

import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow


interface FolderRepository {
    suspend fun getFolder(id: Long): Flow<Result<Folder>>
    suspend fun removeFolder(id: Long): Result<Unit>
}