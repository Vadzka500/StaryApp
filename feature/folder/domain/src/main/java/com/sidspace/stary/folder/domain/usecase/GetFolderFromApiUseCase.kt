package com.sidspace.stary.folder.domain.usecase


import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.folder.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetFolderFromApiUseCase @Inject constructor(private val repository: FolderRepository) {
    suspend operator fun invoke(folder: Folder): Flow<Result<Folder>> =
        repository.getFolderFromApi(folder)
}
