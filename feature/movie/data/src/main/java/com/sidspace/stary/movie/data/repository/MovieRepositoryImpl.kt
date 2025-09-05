package com.sidspace.stary.movie.data.repository


import com.sidspace.stary.data.api.MovieApi
import com.sidspace.stary.data.database.MovieDao
import com.sidspace.stary.data.mapper.toDomain
import com.sidspace.stary.data.mapper.toFolderFromFolderDBO
import com.sidspace.stary.data.mapper.toLocalMovie
import com.sidspace.stary.data.mapper.toMovie
import com.sidspace.stary.data.mapper.toMovieDBO
import com.sidspace.stary.data.model.database.CollectionMovieDBO
import com.sidspace.stary.data.model.database.FolderMovieRef
import com.sidspace.stary.data.utils.safeCall
import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.LocalMovie
import com.sidspace.stary.domain.model.LocalResult
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) : MovieRepository {
    override suspend fun addMovieToFolder(
        idMovie: Long,
        idFolder: Long
    ): Result<Unit> {

        return runCatching {
            movieDao.addMovieToFolder(FolderMovieRef(idMovie, idFolder))
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

    override fun existMovie(idMovie: Long): Flow<LocalResult<LocalMovie>> =
        movieDao.getMovie(idMovie).map { movieDto ->
            movieDto?.toLocalMovie()
        }.map { movie ->
            LocalResult.Success(movie)
        }.catch { error ->
            error.printStackTrace()
            LocalResult.Error
        }.onStart {
            LocalResult.Loading
        }

    override fun getAllFolders(): Flow<Result<List<Folder>>> =
        movieDao.getFolders().map { foldersDbo ->
            foldersDbo.map { it.toFolderFromFolderDBO(it.movies) }
        }.map { folders ->
            Result.Success(folders)
        }.catch { error ->
            Result.Error
        }.onStart {
            Result.Loading
        }

    override fun getMovieById(idMovie: Long): Flow<Result<Movie>> = flow {
        emit(safeCall { movieApi.getMovieById(idMovie) }.toDomain { it.toMovie() })
    }

    override suspend fun removeMovieFromFolder(
        idMovie: Long,
        idFolder: Long
    ): Result<Unit> {

        return runCatching {
            movieDao.removeMovieFromFolder(FolderMovieRef(idMovie, idFolder))
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

    override suspend fun updateMovieBookmark(
        idMovie: Long,
        isBookmark: Boolean
    ): Result<Unit> {

        return runCatching {
            movieDao.updateIsBookmark(idMovie, isBookmark, System.currentTimeMillis())
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

    override suspend fun updateMovieViewed(
        idMovie: Long,
        isViewed: Boolean
    ): Result<Unit> {

        return runCatching {
            movieDao.updateIsViewed(idMovie, isViewed, System.currentTimeMillis())
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

    override suspend fun addMovie(
        movie: LocalMovie,
        listOfCollection: List<String>?
    ): Result<Unit> {

        return runCatching {
            movieDao.addMovie(movie.toMovieDBO())

            listOfCollection?.let {
                it.forEach {
                    movieDao.addMovieCollection(CollectionMovieDBO(movie.movieId, it))
                }
            }
        }.fold(
            onSuccess = {
                Result.Success(Unit)
            },
            onFailure = {
                Result.Error
            }
        )
    }


}
