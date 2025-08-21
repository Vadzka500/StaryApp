package com.sidspace.stary.folders.data.repository

import com.sidspace.stary.data.database.MovieDao

import com.sidspace.stary.folders.data.mapper.toFolderDBO
import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.folders.domain.repository.FoldersRepository
import com.sidspace.stary.data.mapper.toDomainFromDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class FoldersRepositoryImpl @Inject constructor(private val movieDatabase: MovieDao) :
    FoldersRepository {

    override suspend fun addFolder(folder: Folder): Result<Unit> {
        return try {
            movieDatabase.addFolder(folder.toFolderDBO())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error
        }
    }

    override fun getFolders(): Flow<Result<List<Folder>>> = flow {
        emit(Result.Loading)

        try {
            movieDatabase.getFolders().collect {
                emit(Result.Success(it.map { it -> it.toDomainFromDB(it.movies) }))
            }
        } catch (e: Exception) {
            emit(Result.Error)
        }
    }
}