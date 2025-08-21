package com.sidspace.stary.folder.data.repository

import com.sidspace.stary.data.api.MovieApi
import com.sidspace.stary.data.database.MovieDao

import com.sidspace.stary.data.utils.ResultRemote
import com.sidspace.stary.data.utils.safeCall
import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.folder.domain.repository.FolderRepository
import com.sidspace.stary.data.mapper.toDomainFromDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class FolderRepositoryImpl @Inject constructor(
    private val folderDatabase: MovieDao,
    private val movieApi: MovieApi
) : FolderRepository {

    override suspend fun getFolder(id: Long): Flow<Result<Folder>> = flow {

        try {
            val folder = folderDatabase.getFolder(id)
            val movies = safeCall { movieApi.getMoviesByIds(folder.movies.map { it.movieId }) }
            if (movies is ResultRemote.Success) {
                emit(Result.Success(folder.toDomainFromDTO(movies.data.docs)))
            } else {
                emit(Result.Error)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error)
        }
    }

    override suspend fun removeFolder(id: Long): Result<Unit> {
        try {
            folderDatabase.removeFolder(id)
            return Result.Success(Unit)
        } catch (e: Exception) {
            return Result.Error
        }
    }
}