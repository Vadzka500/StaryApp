package com.sidspace.stary.movie.domain.repository

import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.LocalMovie
import com.sidspace.stary.domain.model.LocalResult
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Result
import kotlinx.coroutines.flow.Flow


interface MovieRepository {

    suspend fun addMovieToFolder(idMovie: Long, idFolder: Long): Result<Unit>

    fun existMovie(idMovie: Long): Flow<LocalResult<LocalMovie>>

    fun getAllFolders(): Flow<Result<List<Folder>>>

    fun getMovieById(idMovie: Long): Flow<Result<Movie>>

    suspend fun removeMovieFromFolder(idMovie: Long, idFolder: Long): Result<Unit>

    suspend fun updateMovieBookmark(idMovie: Long, isBookmark: Boolean): Result<Unit>

    suspend fun updateMovieViewed(idMovie: Long, isViewed: Boolean): Result<Unit>

    suspend fun addMovie(movie: LocalMovie, listOfCollection: List<String>?): Result<Unit>
}
