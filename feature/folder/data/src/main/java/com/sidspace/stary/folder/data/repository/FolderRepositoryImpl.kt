package com.sidspace.stary.folder.data.repository

import com.sidspace.stary.data.api.MovieApi
import com.sidspace.stary.data.database.MovieDao
import com.sidspace.stary.data.mapper.toFolderFromFolderDBO
import com.sidspace.stary.data.mapper.toMovie
import com.sidspace.stary.data.utils.ResultRemote
import com.sidspace.stary.data.utils.safeCall
import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.folder.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class FolderRepositoryImpl @Inject constructor(
    private val folderDatabase: MovieDao,
    private val movieApi: MovieApi
) : FolderRepository {
    override fun getFolderFromDb(id: Long): Flow<Result<Folder>> = flow {

        runCatching {
            folderDatabase.getFolder(id).let { it.toFolderFromFolderDBO(it.movies) }
        }.fold(
            onSuccess = { folder ->
                Result.Success(folder)
            },
            onFailure = { error ->
                error.printStackTrace()
                emit(Result.Error)
            }
        )
    }

    override suspend fun getFolderFromApi(folder: Folder): Flow<Result<Folder>> = flow {

        runCatching {
            safeCall { movieApi.getMoviesByIds(folder.listOfMovies!!.map { it.id }) }
        }.fold(
            onSuccess = { result ->
                if (result is ResultRemote.Success) {
                    folder.listOfMovies = result.data.docs.map { it.toMovie() }
                    emit(Result.Success(folder))
                } else {
                    emit(Result.Error)
                }
            },
            onFailure = { error ->
                error.printStackTrace()
                emit(Result.Error)
            }
        )

    }

    override suspend fun removeFolder(id: Long): Result<Unit> {

        return runCatching {
            folderDatabase.removeFolder(id)
        }.fold(
            onSuccess = {
                Result.Success(Unit)
            },
            onFailure = {
                it.printStackTrace()
                Result.Error
            }
        )
    }
}
