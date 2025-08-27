package com.sidspace.stary.domain.model

data class Movie(
    val id: Long,
    val name: String? = null,
    val enName: String? = null,
    val backdropUrl: String? = null,
    val countries: List<String>? = null,
    val description: String? = null,
    val genres: List<String>? = null,
    val isSeries: Boolean? = null,
    val movieLength: Int? = null,
    val previewUrl: String? = null,
    val persons: List<Person>? = null,
    val imdbScore: Double? = null,
    val kpScore: Double? = null,
    val ageRating: Int? = null,
    val releaseYears: List<ReleaseYear>? = null,
    val seasonsInfo: List<SeasonsInfo>? = null,
    val sequelsAndPrequels: List<Movie>? = null,
    val similarMovies: List<Movie>? = null,
    val status: String? = null,
    val trailersUrl: List<Trailer>? = null,
    val year: Int? = null,


    val listOfCollection: List<String>? = null,
    val listOfPerson: List<Person>? = null,
)

data class Trailer(
    val name: String? = null,
    val url: String
)

data class ReleaseYear(
    val end: Int? = null,
    val start: Int? = null
)

data class SeasonsInfo(
    val number: Int
)

