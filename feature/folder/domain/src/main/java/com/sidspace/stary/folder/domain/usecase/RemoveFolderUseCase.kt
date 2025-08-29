package com.sidspace.stary.folder.domain.usecase

import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.folder.domain.repository.FolderRepository
import javax.inject.Inject


class RemoveFolderUseCase @Inject constructor(private val repository: FolderRepository) {

    suspend operator fun invoke(id: Long): Result<Unit> = repository.removeFolder(id)
}
