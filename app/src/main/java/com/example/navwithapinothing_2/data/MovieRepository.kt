package com.example.navwithapinothing_2.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.moviesapi.models.Response
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.api.MovieApi
import com.example.navwithapinothing_2.api.MoviePagingSource
import com.example.navwithapinothing_2.models.Filter
import com.example.navwithapinothing_2.ui.screen.slider.listOfYears
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
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

    fun getRandom(filter: Filter? = null): Flow<Result> {

        val f2 = flow {


            emit(Result.Loading)
        }

        val f = flow {

            println("list1 = " + filter?.listOfCollection)
            println("list2 = " + filter?.year)
            println("list3 = " + filter?.listOfType)
            println("list4 = " + filter?.listOfGenres)
            var years: List<String>? = filter?.year ?: listOf(listOfYears.random())

            filter?.listOfCollection?.let {
                println("not null collection")
                years = filter.year
            }
            println("list5 = $years")
            emit(
                movieService.getRandom(
                    lists = filter?.listOfCollection/*, listYears = years*//*, listsType = filter?.listOfType*/,
                    listGenres = filter?.listOfGenres?.map { it.lowercase() })
            )
        }.map { result ->
            println("isSuccess = " + result.isSuccessful)
            println("isSuccess1 = ${result.message()}")
            if (result.isSuccessful) {
                if (result.body() != null) {
                    println("isSuccess")
                    Result.Success(result.body())
                } else Result.Error
            } else {
                Result.Error
            }
        }


        return merge(f2, f)
    }

    private fun <T> retrofit2.Response<T>.toRequestResult(): Result {
        return if (isSuccessful) {
            if (body() != null)
                Result.Success(body())
            else Result.Error
        } else {
            Result.Error
        }
    }

    fun getMovieById(id: Long): Flow<Result> {
        val flow = flow {

            emit(movieService.getMovieById(id))
        }.map { result ->

            if (result.isSuccessful) {
                Result.Success(result.body())
            } else {
                Result.Error
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
                Result.Error
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
                Result.Error
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
                println("emit = " + result.body().toString())
                Result.Success((result.body() as Response).docs.sortedBy { it.name.contains("top") }
                    .filter { it.name.length < 30 })
            } else {
                Result.Error
            }
        }
        return f
    }

    fun getListMovieByCollection(slug: String): Flow<Result>{
        val f = flow {
            emit(movieService.getMovieByCollection(list = slug))
        }.map { result ->
            if (result.isSuccessful) {
                println("emit")
                println("emit = " + result.body().toString())
                Result.Success((result.body() as Response).docs)
            } else {
                Result.Error
            }
        }
        return f
    }

}

sealed class Result {
    data object Loading : Result()
    data class Success<T>(val data: T) : Result()
    data object Error : Result()
}