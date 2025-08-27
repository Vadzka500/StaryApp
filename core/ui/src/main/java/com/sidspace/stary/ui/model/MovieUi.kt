package com.sidspace.stary.ui.model

data class MovieUi(
    val id: Long,
    val name: String?,
    val enName: String?,
    val previewUrl: String?,
    val scoreKp: Double?,
    val scoreImdb: Double?,
    val isSeries: Boolean,
    val year: Int?,
    val countOfSeasons: Int,
    val releaseStart: Int?,
    val releaseEnd: Int?,
    val listOfGenres: List<String>?,

    val description: String?,
    val backdrop: String?,
    val persons: List<PersonUi>?,
    val sequelsAndPrequels: List<MovieUi>?,
    val similarMovies: List<MovieUi>?,
    val countries: List<String>?,
    val genres: List<String>?,
    val movieLength: Int?,

    val status: String?,
    val ageRating: Int?,

    val collections: List<String>?,

    val trailers: List<TrailerUi>?

)




