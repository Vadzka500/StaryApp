package com.example.navwithapinothing_2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.CollectionMovieDb
import com.example.navwithapinothing_2.database.models.MovieDb
import kotlinx.coroutines.flow.Flow

/**
 * @Author: Vadim
 * @Date: 21.05.2025
 */

@Dao
interface MovieDao {

    @Query("Select * from Movie")
    fun getAllMovies(): Flow<List<MovieDb>>

    @Query("Select * from Movie where idMovie = :id")
    fun getMovie(id: Long): Flow<MovieDb?>

    @Query("Select * from Movie where id in (" +
            "select movieId from CollectionsMovie where collectionSlug = :slug)")
    fun getMovieByCollectionCount(slug: String): Flow<List<MovieDb>>

    @Insert
    suspend fun addMovie(movie: MovieDb): Long

    @Insert
    suspend fun addMovieCollection(collection: CollectionMovieDb)

    @Query("Delete from Movie where idMovie = :id")
    suspend fun removeMovie(id: Long)

}