package com.example.domain.usecase.folder


import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.folders.domain.repository.FoldersRepository

import javax.inject.Inject


class AddFolderUseCase @Inject constructor(private val repository: FoldersRepository) {
    suspend operator fun invoke(folder: Folder): Result<Unit> = repository.addFolder(folder)
}