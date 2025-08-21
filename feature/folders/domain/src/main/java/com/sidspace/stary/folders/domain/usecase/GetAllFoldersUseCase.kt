package com.sidspace.stary.folders.domain.usecase


import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.folders.domain.repository.FoldersRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllFoldersUseCase @Inject constructor(private val repository: FoldersRepository) {
    operator fun invoke(): Flow<Result<List<Folder>>> = repository.getFolders()
}