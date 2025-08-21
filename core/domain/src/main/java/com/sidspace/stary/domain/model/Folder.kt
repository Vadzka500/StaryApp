package com.sidspace.stary.domain.model


data class Folder(
    val id: Long = 0,
    val name: String,
    val color:Int,
    val imageResName: String?,
    val listOfMovies: List<Movie>? = null
)
