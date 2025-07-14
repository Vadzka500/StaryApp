package com.example.navwithapinothing_2.data


import com.example.moviesapi.models.Response
import com.example.navwithapinothing_2.api.MovieApi

import com.example.navwithapinothing_2.utils.RandomFiltersOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import java.util.Locale
import javax.inject.Inject


class MovieRepository @Inject constructor(private val movieService: MovieApi) {

    fun getRandom(filter: RandomFiltersOption? = null): Flow<Result> {


        val flow = flow{

            val years = filter!!.years?.joinToString("-")
            val type = filter.listOfType?.map { it.value }
            val genres = filter.listOfGenres?.map { it.lowercase() }
            val score = filter.listOfScore?.joinToString("-")

            println("year = " + years)
            println("type = " + type)
            println("genres = " + genres)
            println("score = " + score)

            emit(movieService.getRandom(
                listYears = years,
                listsType = type,
                listGenres = genres,
                score = score
            ))
        }.map { result ->

            if (result.isSuccessful) {
                if (result.body() != null) {
                    println("isSuccess")
                    Result.Success(result.body())
                } else Result.Error(result.code())
            } else {
                println("err1 = " + result.errorBody()!!.string())
                Result.Error(result.code())
            }
        }


        return flow
    }

    fun getMovieById(id: Long): Flow<Result> {
        val flow = flow {
            emit(movieService.getMovieById(id))
        }.map { result ->

            if (result.isSuccessful) {
                Result.Success(result.body())
            } else {
                Result.Error(result.code())
            }


        }

        return flow
    }


    fun getReviewsById(id: Long): Flow<Result>{
        val flow = flow {
            emit(movieService.getReviewsById(id))
        }.map { result ->
            if(result.isSuccessful){
                Result.Success((result.body() as Response).docs)
            }else{
                println("Error = " + result.message())
                Result.Error(result.body())
            }
        }
        return flow
    }

    fun getMoviesByIds(ids: List<Long>): Flow<Result> {
        val flow = flow {
            emit(movieService.getMoviesByIds((ids)))
        }.map { result ->

            if (result.isSuccessful) {
                Result.Success((result.body() as Response).docs)
            } else {
                Result.Error(result.message())
            }

        }

        return flow
    }


    fun getPersonById(id: Long): Flow<Result> {
        val flow = flow {
            emit(movieService.getPersonById(id))
        }.map {
            if (it.isSuccessful) {
                Result.Success(it.body())
            } else {
                Result.Error(it.code())
            }
        }
        return flow
    }

    fun getSearchedMovies(search: String): Flow<Result>{
        val flow = flow{
            emit(movieService.getAllMovies(search = search))
        }.map { result ->
            if(result.isSuccessful){
                Result.Success(result.body())
            }else{
                Result.Error(result.code())
            }
        }
        return flow
    }

    fun getMoviesByPerson(id: Long): Flow<Result> {
        val flow = flow {
            emit(movieService.getMovieByPerson(id))
        }.map {
            if (it.isSuccessful) {
                Result.Success(it.body())
            } else {
                Result.Error(it.code())
            }
        }
        return flow
    }

    fun getCollections(): Flow<Result> {
        val f = flow {

            emit(movieService.getCollections())
        }.map { result ->
            if (result.isSuccessful) {
                println("emit")

                Result.Success((result.body() as Response).docs.sortedBy { it.name.contains("top") }
                    .filter { it.name.length < 45 })
            } else {
                Result.Error(result.code())
            }
        }
        return f
    }



    fun getListMovieByCollection(slug: String, limit: Int = 30): Flow<Result>{
        val f = flow {
            emit(movieService.getMovieByCollection(list = slug, limit = limit))
        }.map { result ->
            println("emit 0 = $slug")
            if (result.isSuccessful) {
                println("emit 1")

                Result.Success((result.body() as Response).docs)
            } else {
                println("errors = " + result.code())
                Result.Error(result.code())
            }
        }
        return f
    }

}

sealed class Result {
    data object Loading : Result()
    data class Success<T>(val data: T) : Result()
    data class Error<String>(val data: String) : Result()
}