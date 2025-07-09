package com.example.navwithapinothing_2.database

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.navwithapinothing_2.database.models.CollectionMovieDb
import com.example.navwithapinothing_2.database.models.Folder
import com.example.navwithapinothing_2.database.models.FolderMovieRef
import com.example.navwithapinothing_2.database.models.MovieDb

/**
 * @Author: Vadim
 * @Date: 21.05.2025
 */
@Database(entities = [MovieDb::class, CollectionMovieDb::class, Folder::class, FolderMovieRef::class], version = 17)

abstract class MovieDatabase: RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}