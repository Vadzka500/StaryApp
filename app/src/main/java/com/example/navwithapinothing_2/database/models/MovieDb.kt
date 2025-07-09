package com.example.navwithapinothing_2.database.models

import androidx.annotation.DrawableRes
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.sql.Date

/**
 * @Author: Vadim
 * @Date: 21.05.2025
 */

@Entity(tableName = "Movie")
data class MovieDb(
    @PrimaryKey
    val movieId: Long,

    var isViewed: Boolean = false,
    var dateViewed: Long? = null,

    var isBookmark: Boolean = false,
    var dateBookmark: Long? = null

)

@Entity(tableName = "CollectionsMovie", primaryKeys = ["movieId", "collectionSlug"])
data class CollectionMovieDb(
    var movieId: Long,
    val collectionSlug: String
)

@Entity(tableName = "Folder")
data class Folder(
    @PrimaryKey(autoGenerate = true) var folderId: Long = 0,
    var folderName: String,
    val color: Int,
    val imageResName: String?

)

@Entity(primaryKeys = ["movieId", "folderId"])
data class FolderMovieRef(
    val movieId: Long,
    val folderId: Long
)


data class FolderWithMovies(
    @Embedded val folder: Folder,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "movieId",
        associateBy = Junction(
            value = FolderMovieRef::class,
            parentColumn = "folderId",
            entityColumn = "movieId")
    )
    val movies: List<MovieDb>
)
