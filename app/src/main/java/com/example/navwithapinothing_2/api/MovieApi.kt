package com.example.navwithapinothing_2.api

import com.example.moviesapi.models.movie.MovieDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieApi {

    @GET("movie/search")
    suspend fun getAllMovies(
        @Query("limit") limit: Int = 100,
        @Query("page") page: Int
    ):com.example.moviesapi.models.Response<MovieDTO>

    @GET("movie/random")
    suspend fun getRandom(
        //@Query("limit") limit: Int = 100,
        //@Query("page") page: Int
        @Query("notNullFields") field : String = "poster.url",
        @Query("lists") lists: List<String> = listOf("top500")
    ):Response<MovieDTO>

}