package com.sidspace.stary.data.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sidspace.stary.data.model.database.CollectionMovieDBO
import com.sidspace.stary.data.model.database.FolderDBO
import com.sidspace.stary.data.model.database.FolderMovieRef
import com.sidspace.stary.data.model.database.FolderWithMoviesDBO

import com.sidspace.stary.data.model.database.MovieDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("Select * from Movie")
    fun getAllMovies(): Flow<List<MovieDBO>>

    @Query("Select * from Movie where isViewed = true order by dateViewed desc")
    fun getViewedMovies(): Flow<List<MovieDBO>>

    @Query("Select * from Movie where isBookmark = true order by dateBookmark desc")
    fun getBookmarkMovies(): Flow<List<MovieDBO>>

    @Query("Select * from Movie where movieId = :id")
    fun getMovie(id: Long): Flow<MovieDBO?>

    @Query("Select Count(*) from Movie where movieId = :id")
    suspend fun getCountMovieById(id: Long): Long

    @Query("Select Movie.* from Movie " +
            "inner join CollectionsMovie on CollectionsMovie.movieId = Movie.movieId " +
            "where collectionSlug = :slug and isViewed = true")
    fun getMovieByCollectionCount(slug: String): Flow<List<MovieDBO>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addMovie(movie: MovieDBO): Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun addMovieCollection(collection: CollectionMovieDBO)

    @Query("Delete from Movie where movieId = :id")
    suspend fun removeMovie(id: Long)

    @Query("Delete from Folder where folderId = :id")
    suspend fun removeFolder(id: Long)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun addFolder(folder: FolderDBO)

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addFolders(list: List<FolderDBO>)

    @Query("Select count(*) From Folder")
    fun getCountFolders(): Flow<Long>

    @Transaction
    @Query("Select * from Folder order by folderId desc")
    fun getFolders(): Flow<List<FolderWithMoviesDBO>>

    @Query("Select * from Folder where folderId = :id")
    suspend fun getFolder(id: Long): FolderWithMoviesDBO

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addMovieToFolder(ref: FolderMovieRef)

    @Delete
    suspend fun removeMovieFromFolder(ref: FolderMovieRef)

    @Query("Update Movie Set isViewed = :isViewed, dateViewed = :date where movieId = :id")
    suspend fun updateIsViewed(id: Long, isViewed: Boolean, date : Long)

    @Query("Update Movie Set isBookmark = :isBookmark, dateBookmark = :date where movieId = :id")
    suspend fun updateIsBookmark(id: Long, isBookmark: Boolean, date: Long)
}