package com.sidspace.stary.domain.model

data class Person(
    //val description: String? = null,
    //val enName: String? = null,
    //val enProfession: String? = null,
    val id: Long,
    val name: String? = null,
    val photo: String? = null,
    val profession: String? = null,
    //val sex: String? = null,
    val growth: Int? = null,
    val birthday: String? = null,
    //val death: String? = null,
    val age: Int? = null,
    val moviesIds: List<Long>? = null,
)
