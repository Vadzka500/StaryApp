package com.example.navwithapinothing_2.data

import com.example.navwithapinothing_2.database.MovieDao
import com.example.navwithapinothing_2.database.models.CollectionMovieDb
import com.example.navwithapinothing_2.database.models.Folder
import com.example.navwithapinothing_2.database.models.FolderMovieRef
import com.example.navwithapinothing_2.database.models.MovieDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.sql.Date
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 21.05.2025
 */
class MovieDatabaseRepository @Inject constructor(private val movieDao: MovieDao) {

    fun getViewedMovies(): Flow<ResultDb<List<MovieDb>>> = flow {
        emit(ResultDb.Loading)

        try {
            movieDao.getViewedMovies().collect {
                emit(ResultDb.Success(it))
            }
        } catch (e: Exception) {
            emit(ResultDb.Error)
        }

    }

    fun getBookmarkMovies():Flow<ResultDb<List<MovieDb>>> = flow{
        emit(ResultDb.Loading)
        try{
            movieDao.getBookmarkMovies().collect {
                emit(ResultDb.Success(it))
            }
        }catch (e: Exception){
            emit(ResultDb.Error)
        }
    }

    fun getMoviesByCollection(collection: String): Flow<ResultDb<List<MovieDb>>> = flow {
        emit(ResultDb.Loading)

        try{
            movieDao.getMovieByCollectionCount(collection).collect{
                println("sssize = " + it.size)
                emit(ResultDb.Success(it))
            }
        }catch (e: Exception){
            println("sssize 2 = " + e.printStackTrace())
            emit(ResultDb.Error)
        }
    }

    fun getFolders() = flow {
        emit(ResultDb.Loading)

        try{
            movieDao.getFolders().collect {
                emit(ResultDb.Success(it))
            }
        }catch (e: Exception){
            emit(ResultDb.Error)
        }
    }

    suspend fun getFolder(id: Long): ResultDb<Folder>{
        return try{
            ResultDb.Success(movieDao.getFolder(id))
        }catch (e: Exception){
            ResultDb.Error
        }
    }

    suspend fun getFoldersCount(): Long{
        return movieDao.getCountFolders()
    }

    suspend fun addFolders(listOfFolders: List<Folder>): ResultDb<Unit>{
        return try{
            movieDao.addFolders(listOfFolders)
            ResultDb.Success(Unit)
        }catch (e: Exception){
            ResultDb.Error
        }
    }

    suspend fun addMovieToFolder(idMovie: Long, idFolder:Long): ResultDb<Unit>{
        return try{
            movieDao.addMovieToFolder(FolderMovieRef(idMovie, idFolder))
            ResultDb.Success(Unit)
        }catch (e: Exception){
            e.printStackTrace()
            ResultDb.Error
        }
    }

    suspend fun removeMovieFromFolder(idMovie: Long, idFolder:Long): ResultDb<Unit>{
        return try{
            movieDao.removeMovieFromFolder(FolderMovieRef(idMovie, idFolder))
            ResultDb.Success(Unit)
        }catch (e: Exception){
            ResultDb.Error
        }
    }

    suspend fun addFolder(folder: Folder): ResultDb<Unit>{
        return try{
            movieDao.addFolder(folder)
            ResultDb.Success(Unit)
        }catch (e: Exception){
            ResultDb.Error
        }
    }

    suspend fun addMovie(movie: MovieDb, list: List<CollectionMovieDb>): ResultDb<Unit> {
        return try {
            movieDao.addMovie(movie)
            for(collection in list){
                movieDao.addMovieCollection(collection)
            }

            ResultDb.Success(Unit)
        }catch (e: Exception){
            ResultDb.Error
        }
    }

    suspend fun removeMovie(id: Long): ResultDb<Unit>{
        return try{
            movieDao.removeMovie(id)
            ResultDb.Success(Unit)
        }catch (e: Exception){
            ResultDb.Error
        }
    }

    suspend fun updateInViewed(id: Long, isViewed: Boolean): ResultDb<Unit>{
        return try{
            movieDao.updateIsViewed(id, isViewed, System.currentTimeMillis())
            ResultDb.Success(Unit)
        }catch (e: Exception){
            ResultDb.Error
        }
    }

    suspend fun updateInBookmark(id: Long, isBookmark: Boolean): ResultDb<Unit>{
        return try{
            movieDao.updateIsBookmark(id, isBookmark, System.currentTimeMillis())
            ResultDb.Success(Unit)
        }catch (e: Exception){
            ResultDb.Error
        }
    }

    suspend fun getMovieById(id: Long): Boolean{
        return try{
            val count = movieDao.getCountMovieById(id)
            count > 0L
        }catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    fun checkMovie(id: Long): Flow<ResultDb<MovieDb?>> = flow{
        emit(ResultDb.Loading)
        try{
            movieDao.getMovie(id).collect{
                emit(ResultDb.Success(it))
            }
        }catch (e : Exception){
            println(e.message)
            emit(ResultDb.Error)
        }
    }

}

sealed class ResultDb<out T> {
    data class Success<out T>(val data: T) : ResultDb<T>()
    data object Error : ResultDb<Nothing>()
    data object Loading : ResultDb<Nothing>()
}