package com.example.navwithapinothing_2.models.movie

import kotlinx.serialization.Serializable

/**
 * @Author: Vadim
 * @Date: 11.02.2025
 */
@Serializable
data class PersonOfMovie(
    val description: String? = null,
    val enName: String? = null,
    val enProfession: String? = null,
    val id: Long,
    val name: String? = null,
    val photo: String? = null,
    val profession: String? = null,
)
