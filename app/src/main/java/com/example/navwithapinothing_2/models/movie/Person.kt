package com.example.moviesapi.models.movie

import com.example.navwithapinothing_2.models.movie.MovieByPerson
import com.example.navwithapinothing_2.models.movie.Profession
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val description: String? = null,
    val enName: String? = null,
    val enProfession: String? = null,
    val id: Long,
    val name: String? = null,
    val photo: String? = null,
    val profession: List<Profession>? = null,
    val sex: String? = null,
    val growth: Int? = null,
    val birthday: String? = null,
    val death: String? = null,
    val age: Int? = null,
    val movies: List<MovieByPerson>? = null,
)

