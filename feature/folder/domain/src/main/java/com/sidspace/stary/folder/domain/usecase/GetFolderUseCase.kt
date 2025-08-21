package com.example.domain.usecase.folder


import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.folder.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetFolderUseCase @Inject constructor(private val repository: FolderRepository) {
    suspend operator fun invoke(id: Long) : Flow<Result<Folder>> = repository.getFolder(id)
}