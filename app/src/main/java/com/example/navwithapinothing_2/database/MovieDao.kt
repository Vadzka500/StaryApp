package com.example.navwithapinothing_2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.CollectionMovieDb
import com.example.navwithapinothing_2.database.models.Folder
import com.example.navwithapinothing_2.database.models.FolderMovieRef
import com.example.navwithapinothing_2.database.models.FolderWithMovies
import com.example.navwithapinothing_2.database.models.MovieDb
import kotlinx.coroutines.flow.Flow
import java.sql.Date

/**
 * @Author: Vadim
 * @Date: 21.05.2025
 */

@Dao
interface MovieDao {

    @Query("Select * from Movie")
    fun getAllMovies(): Flow<List<MovieDb>>

    @Query("Select * from Movie where isViewed = true order by dateViewed desc")
    fun getViewedMovies(): Flow<List<MovieDb>>

    @Query("Select * from Movie where isBookmark = true order by dateBookmark desc")
    fun getBookmarkMovies(): Flow<List<MovieDb>>

    @Query("Select * from Movie where movieId = :id")
    fun getMovie(id: Long): Flow<MovieDb?>

    @Query("Select Count(*) from Movie where movieId = :id")
    suspend fun getCountMovieById(id: Long): Long

   /* @Query("Select * from Movie where idMovie in (" +
            "select movieId from CollectionsMovie where collectionSlug = :slug)")*/

    @Query("Select Movie.* from Movie " +
            "inner join CollectionsMovie on CollectionsMovie.movieId = Movie.movieId " +
            "where collectionSlug = :slug and isViewed = true")
    fun getMovieByCollectionCount(slug: String): Flow<List<MovieDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: MovieDb): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieCollection(collection: CollectionMovieDb)

    @Query("Delete from Movie where movieId = :id")
    suspend fun removeMovie(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFolder(folder: Folder)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFolders(list: List<Folder>)

    @Query("Select count(*) From Folder")
    suspend fun getCountFolders(): Long

    @Transaction
    @Query("Select * from Folder order by folderId desc")
    fun getFolders(): Flow<List<FolderWithMovies>>

    @Query("Select * from Folder where folderId = :id")
    suspend fun getFolder(id: Long): Folder

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovieToFolder(ref: FolderMovieRef)

    @Delete
    suspend fun removeMovieFromFolder(ref: FolderMovieRef)

    @Query("Update Movie Set isViewed = :isViewed, dateViewed = :date where movieId = :id")
    suspend fun updateIsViewed(id: Long, isViewed: Boolean, date : Long)

    @Query("Update Movie Set isBookmark = :isBookmark, dateBookmark = :date where movieId = :id")
    suspend fun updateIsBookmark(id: Long, isBookmark: Boolean, date: Long)
}