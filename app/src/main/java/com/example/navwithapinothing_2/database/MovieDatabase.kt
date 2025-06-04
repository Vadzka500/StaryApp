package com.example.navwithapinothing_2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.navwithapinothing_2.database.models.CollectionMovieDb
import com.example.navwithapinothing_2.database.models.MovieDb
import com.example.navwithapinothing_2.models.collection.CollectionMovie

/**
 * @Author: Vadim
 * @Date: 21.05.2025
 */
@Database(entities = [MovieDb::class, CollectionMovieDb::class], version = 6)
@TypeConverters(MovieDbConverter::class)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}