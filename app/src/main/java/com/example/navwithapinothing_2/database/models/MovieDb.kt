package com.example.navwithapinothing_2.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author: Vadim
 * @Date: 21.05.2025
 */

@Entity(tableName = "Movie")
data class MovieDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val idMovie: Long,
    var isViewed: Boolean = false,
    var isBookmark: Boolean = false
)

@Entity(tableName = "CollectionsMovie")
data class CollectionMovieDb(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var movieId: Int = 0,
    val collectionSlug: String
)
