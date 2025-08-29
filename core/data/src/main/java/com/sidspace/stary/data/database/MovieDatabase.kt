package com.sidspace.stary.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.sidspace.stary.data.model.database.CollectionMovieDBO
import com.sidspace.stary.data.model.database.FolderDBO
import com.sidspace.stary.data.model.database.FolderMovieRef
import com.sidspace.stary.data.model.database.MovieDBO


@Database(
    entities = [MovieDBO::class, CollectionMovieDBO::class, FolderDBO::class, FolderMovieRef::class],
    version = 17
)

abstract class MovieDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}
