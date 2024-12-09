package com.example.navwithapinothing_2.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.moviesapi.models.Response
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.api.MovieApi
import com.example.navwithapinothing_2.api.MoviePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class MovieRepository @Inject constructor(private val movieService: MovieApi) {

    fun getAll() = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { MoviePagingSource(movieService) }
    ).flow

    /* fun getAll(): Flow<Result> {
         val f =  flow {
             emit(movieService.getAllMovies())
         }.map { result ->
             if(result.isSuccessful){
                 println("body = " + result.body().toString())
                 Result.Success(result.body()!!)
             }else{
                 println("error = " + result.message())
                 Result.Error
             }
         }
         return f
     }*/

    fun getRandom(): Flow<Result> {
        val f = flow {

            emit(movieService.getRandom())
        }.map { result ->
            if(result.isSuccessful){
                println("emit")
                Result.Success(result.body())
            }else{
                Result.Error
            }
        }
        return f
    }

}

sealed class Result{
    data object Loading : Result()
    data class Success<T>(val data: T) : Result()
    data object Error : Result()
}