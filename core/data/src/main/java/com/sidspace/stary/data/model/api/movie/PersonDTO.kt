package com.sidspace.stary.data.model.api.movie

import kotlinx.serialization.Serializable

@Serializable
data class PersonDTO(
    val description: String? = null,
    val enName: String? = null,
    val enProfession: String? = null,
    val id: Long,
    val name: String? = null,
    val photo: String? = null,
    val profession: List<String>? = null,
    val sex: String? = null,
    val growth: Int? = null,
    val birthday: String? = null,
    val death: String? = null,
    val age: Int? = null,
    val movies: List<MovieByPersonDTO>? = null,
)

@Serializable
data class PersonOfMovieDTO(
    val description: String? = null,
    val enName: String? = null,
    val enProfession: String? = null,
    val id: Long,
    val name: String? = null,
    val photo: String? = null,
    val profession: String? = null,
)
