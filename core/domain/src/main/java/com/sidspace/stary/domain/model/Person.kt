package com.sidspace.stary.domain.model

data class Person(
    val id: Long,
    val name: String? = null,
    val photo: String? = null,
    val profession: List<String>? = null,
    val growth: Int? = null,
    val birthday: String? = null,
    val age: Int? = null,
    val moviesIds: List<Long>? = null,
)
