package com.example.navwithapinothing_2.models.collection

import kotlinx.serialization.Serializable

@Serializable
data class CollectionMovie(
    val category: String,
    val cover: Cover? = null,
    val createdAt: String,
    val id: String,
    val moviesCount: Int? = null,
    val name: String,
    val slug: String,
    val updatedAt: String,
    var viewedCount: Int = 0
)