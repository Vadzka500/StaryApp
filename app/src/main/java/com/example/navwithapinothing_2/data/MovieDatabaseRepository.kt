package com.example.navwithapinothing_2.data

import com.example.navwithapinothing_2.database.MovieDao
import com.example.navwithapinothing_2.database.MovieDatabase
import com.example.navwithapinothing_2.database.models.CollectionMovieDb
import com.example.navwithapinothing_2.database.models.MovieDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 21.05.2025
 */
class MovieDatabaseRepository @Inject constructor(private val movieDao: MovieDao) {

    fun getAllMovies(): Flow<ResultDb<List<MovieDb>>> = flow {
        emit(ResultDb.Loading)

        try {
            movieDao.getAllMovies().collect {
                emit(ResultDb.Success(it))
            }
        } catch (e: Exception) {
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

    suspend fun addMovie(movie: MovieDb, list: List<CollectionMovieDb>): ResultDb<Unit> {
        return try {
            val id = movieDao.addMovie(movie)
            for(collection in list){
                collection.movieId = id.toInt()
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