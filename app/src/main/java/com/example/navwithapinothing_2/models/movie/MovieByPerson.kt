package com.example.navwithapinothing_2.models.movie

import kotlinx.serialization.Serializable

/**
 * @Author: Vadim
 * @Date: 11.02.2025
 */
@Serializable
data class MovieByPerson(
    val id: Long? = null,
    val name: String? = null,
    val alternativeName: String? = null,
    val rating: Double? = null,
    val general: Boolean? = null,
    val description: String? = null,
    val enProfession: String? = null
)