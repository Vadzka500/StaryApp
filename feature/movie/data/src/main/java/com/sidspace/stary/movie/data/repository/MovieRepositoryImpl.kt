package com.sidspace.stary.movie.data.repository


import com.sidspace.stary.core.data.api.MovieApi
import com.sidspace.stary.core.data.database.MovieDao

import com.sidspace.stary.core.data.model.database.CollectionMovieDBO
import com.sidspace.stary.core.data.model.database.FolderMovieRef
import com.sidspace.stary.core.data.utils.safeCall
import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.LocalMovie
import com.sidspace.stary.domain.model.LocalResult
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.movie.domain.repository.MovieRepository
import com.sidspace.stary.core.data.mapper.toDomain
import com.sidspace.stary.core.data.mapper.toDomainFromDB
import com.sidspace.stary.core.data.mapper.toLocalMovie
import com.sidspace.stary.core.data.mapper.toMovie
import com.sidspace.stary.core.data.mapper.toMovieDBO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) : MovieRepository {
    override suspend fun addMovieToFolder(
        idMovie: Long,
        idFolder: Long
    ): Result<Unit> {
        return try {
            movieDao.addMovieToFolder(FolderMovieRef(idMovie, idFolder))
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error
        }
    }

    override fun existMovie(idMovie: Long): Flow<LocalResult<LocalMovie>> = flow {
        emit(LocalResult.Loading)

        try {
            movieDao.getMovie(idMovie).collect {
                emit(LocalResult.Success(it?.toLocalMovie()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(LocalResult.Error)
        }
    }

    override fun getAllFolders(): Flow<Result<List<Folder>>> = flow {

        emit(Result.Loading)

        try {
            movieDao.getFolders().collect {
                emit(Result.Success(it.map{ it.toDomainFromDB(it.movies)}))
            }
        } catch (e: Exception) {
            emit(Result.Error)
        }
    }

    override fun getMovieById(idMovie: Long): Flow<Result<Movie>> = flow {
        emit(safeCall { movieApi.getMovieById(idMovie) }.toDomain { it.toMovie() })
    }

    override suspend fun removeMovieFromFolder(
        idMovie: Long,
        idFolder: Long
    ): Result<Unit> {
       return try{
           movieDao.removeMovieFromFolder(FolderMovieRef(idMovie, idFolder))
           Result.Success(Unit)
       }catch (e: Exception){
           Result.Error
       }
    }

    override suspend fun updateMovieBookmark(
        idMovie: Long,
        isBookmark: Boolean
    ): Result<Unit> {
        return try{
            movieDao.updateIsBookmark(idMovie, isBookmark, System.currentTimeMillis())
            Result.Success(Unit)
        }catch (e: Exception){
            Result.Error
        }
    }

    override suspend fun updateMovieViewed(
        idMovie: Long,
        isViewed: Boolean
    ): Result<Unit> {
        return try{
            movieDao.updateIsViewed(idMovie, isViewed, System.currentTimeMillis())
            Result.Success(Unit)
        }catch (e: Exception){
            Result.Error
        }
    }

    override suspend fun addMovie(
        movie: LocalMovie,
        listOfCollection: List<String>?
    ): Result<Unit> {
        return try {
            movieDao.addMovie(movie.toMovieDBO())

            listOfCollection?.let {
                it.forEach {
                    movieDao.addMovieCollection(CollectionMovieDBO(movie.movieId, it))
                }
            }


            Result.Success(Unit)
        }catch (e: Exception){
            Result.Error
        }
    }


}