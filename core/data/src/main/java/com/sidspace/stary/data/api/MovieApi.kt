package com.sidspace.stary.data.api

import androidx.annotation.IntRange

import com.sidspace.stary.data.model.api.collection.CollectionMovie
import com.sidspace.stary.data.model.api.movie.MovieDTO
import com.sidspace.stary.data.model.api.movie.PersonDTO
import com.sidspace.stary.data.model.api.review.UserReview

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApi {

    @GET("movie/search")
    suspend fun getAllMovies(
        @Query("limit") limit: Int = 50,
        @Query("page") page: Int = 1,
        @Query("query") search: String = ""
    ): Response<com.sidspace.stary.data.model.api.Response<MovieDTO>>


    @GET("movie/random")
    suspend fun getRandom(
        @Query("notNullFields") field: List<String> = listOf("poster.url", "name", "year"),
        @Query("lists") lists: List<String>? = null,
        @Query("type") listsType: List<String>? = null,
        @Query("year") listYears: String? = null,
        @Query("genres.name") listGenres: List<String>? = null,
        @Query("rating.kp") score: String? = null
    ): Response<MovieDTO>

    @GET("list")
    suspend fun getCollections(
        @Query("limit") @IntRange(
            from = 10,
            to = 250
        ) limit: Int = 250,
        @Query("sortField") sort: String = "moviesCount",
        @Query("sortType") sortType: String = "-1",
        @Query("category") categories: List<String> = listOf("Фильмы", "Сериалы")
    ): Response<com.sidspace.stary.data.model.api.Response<CollectionMovie>>

    @GET("movie/{id}")
    suspend fun getMovieById(
        @Path("id") id: Long
    ): Response<MovieDTO>

    @GET("movie")
    suspend fun getMoviesByIds(
        @Query("id") id: List<Long>,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 250,
    ): Response<com.sidspace.stary.data.model.api.Response<MovieDTO>>


    @GET("review")
    suspend fun getReviewsById(
        @Query("movieId") id: Long,
        @Query("page") page: Int = 1,
        @Query("sortField") sort: String = "date",
        @Query("sortType") type: Int = -1,
        @Query("limit") limit: Int = 250,
    ): Response<com.sidspace.stary.data.model.api.Response<UserReview>>

    @GET("person/{id}")
    suspend fun getPersonById(
        @Path("id") id: Long
    ): Response<PersonDTO>

    @GET("movie")
    suspend fun getMovieByPerson(
        @Query("persons.id") id: Long,
        @Query("limit") @IntRange(
            from = 10,
            to = 250
        ) limit: Int = 250,
    ): Response<com.sidspace.stary.data.model.api.Response<MovieDTO>>


    @GET("movie")
    suspend fun getMovieByCollection(
        @Query("limit") @IntRange(
            from = 10,
            to = 250
        ) limit: Int = 30,
        @Query("notNullFields") field: List<String> = listOf("poster.url", "name"),
        @Query("lists") list: String
    ): Response<com.sidspace.stary.data.model.api.Response<MovieDTO>>
}
