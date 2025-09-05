package com.sidspace.stary.folders.data.repository

import com.sidspace.stary.data.database.MovieDao
import com.sidspace.stary.data.mapper.toFolderFromFolderDBO
import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.folders.data.mapper.toFolderDBO
import com.sidspace.stary.folders.domain.repository.FoldersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


class FoldersRepositoryImpl @Inject constructor(private val movieDatabase: MovieDao) :
    FoldersRepository {

    override suspend fun addFolder(folder: Folder): Result<Unit> {

        return runCatching {
            movieDatabase.addFolder(folder.toFolderDBO())
        }.fold(
            onSuccess = {
                Result.Success(Unit)
            },
            onFailure = { error ->
                error.printStackTrace()
                Result.Error
            }
        )
    }

    override fun getFolders(): Flow<Result<List<Folder>>> =
        movieDatabase.getFolders().map { list ->
            list.map { item -> item.toFolderFromFolderDBO(item.movies) }
        }.map { folders ->
            Result.Success(folders)
        }.catch { e ->
            e.printStackTrace()
            Result.Error
        }.onStart {
            Result.Loading
        }

   /* override fun getFolders(): Flow<Result<List<Folder>>> = flow {
        emit(Result.Loading)



        try {
            movieDatabase.getFolders().collect {
                emit(Result.Success(it.map { item -> item.toFolderFromFolderDBO(item.movies) }))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error)
        }
    }*/
}
