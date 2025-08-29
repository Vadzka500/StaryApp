package com.sidspace.stary.movie.domain.usecase

import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFoldersUseCase @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(): Flow<Result<List<Folder>>> = repository.getAllFolders()
}
