package com.example.domain.usecase.movie

import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.movie.domain.repository.MovieRepository
import javax.inject.Inject


class RemoveMovieFromFolderUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend operator fun invoke(idMovie:Long, idFolder:Long): Result<Unit> = repository.removeMovieFromFolder(idMovie, idFolder)
}