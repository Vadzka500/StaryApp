package com.sidspace.stary.data.model.database



import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "Folder")
data class FolderDBO(
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


data class FolderWithMoviesDBO(
    @Embedded val folder: FolderDBO,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "movieId",
        associateBy = Junction(
            value = FolderMovieRef::class,
            parentColumn = "folderId",
            entityColumn = "movieId")
    )
    val movies: List<MovieDBO>
)

@Entity(tableName = "Movie")
data class MovieDBO(
    @PrimaryKey
    val movieId: Long,

    var isViewed: Boolean = false,
    var dateViewed: Long? = null,

    var isBookmark: Boolean = false,
    var dateBookmark: Long? = null

)

@Entity(tableName = "CollectionsMovie", primaryKeys = ["movieId", "collectionSlug"])
data class CollectionMovieDBO(
    var movieId: Long,
    val collectionSlug: String
)

