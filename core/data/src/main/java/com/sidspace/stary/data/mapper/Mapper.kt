package com.sidspace.stary.data.mapper


import com.sidspace.stary.domain.model.Collection
import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.domain.model.LocalMovie
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Person
import com.sidspace.stary.domain.model.ReleaseYear
import com.sidspace.stary.domain.model.SeasonsInfo

import com.sidspace.stary.data.model.api.collection.CollectionMovie
import com.sidspace.stary.data.model.api.movie.MovieDTO
import com.sidspace.stary.data.model.api.movie.PersonDTO
import com.sidspace.stary.data.model.api.movie.PersonOfMovieDTO
import com.sidspace.stary.data.model.database.FolderWithMoviesDBO
import com.sidspace.stary.data.model.database.MovieDBO
import com.sidspace.stary.data.utils.ResultRemote
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.domain.model.Trailer


suspend fun <T, R> ResultRemote<T>.toDomain(transform: suspend (T) -> R): Result<R> {
    return when (this) {
        is ResultRemote.Error<*> -> {
            Result.Error
        }

        is ResultRemote.Loading -> {
            Result.Loading
        }

        is ResultRemote.Success -> {
            Result.Success(transform(data))
        }
    }
}

fun MovieDTO.toMovie(): Movie {
    return Movie(
        id = id!!,
        name = name,
        enName = enName,
        previewUrl = poster?.previewUrl,
        backdropUrl = backdrop?.url,
        countries = countries?.map { it.name },
        description = description,
        genres = genres?.map { it.name!! },
        isSeries = isSeries,
        movieLength = movieLength,
        persons = persons?.map { it.toPerson() },
        imdbScore = rating?.imdb,
        kpScore = rating?.kp,
        ageRating = ageRating,
        releaseYears = releaseYears?.map { ReleaseYear(it.start, it.end) },
        year = year,
        seasonsInfo = seasonsInfo?.map { SeasonsInfo(it.number) },
        sequelsAndPrequels = sequelsAndPrequels?.map { it.toMovie() },
        similarMovies = similarMovies?.map { it.toMovie() },
        status = status,
        trailersUrl = videos?.trailers?.map { Trailer(it.name, it.url!!) },
        listOfCollection = lists,
        listOfPerson = persons?.map{it.toPerson()}
    )
}

fun PersonDTO.toPerson(): Person{
    return Person(
        id = id,
        name = name,
        photo = photo,
        profession = profession,
        growth = growth,
        birthday = birthday,
        age = age,
        moviesIds = movies?.map { it.id }
    )
}

fun PersonOfMovieDTO.toPerson(): Person{
    return Person(
        id = id,
        name = name,
        photo = photo,
        profession = profession?.let { listOf(it) },
    )
}

fun MovieDBO.toLocalMovie(): LocalMovie{
    return LocalMovie(
        movieId = movieId,
        isViewed = isViewed,
        dateViewed = dateViewed,
        isBookmark = isBookmark,
        dateBookmark = dateBookmark

    )
}

fun MovieDBO.toMovie(): Movie{
    return Movie(
        id = movieId,
    )
}

fun LocalMovie.toMovieDBO(): MovieDBO{
    return MovieDBO(
        movieId = movieId,
        isViewed = isViewed,
        dateViewed = dateViewed,
        isBookmark = isBookmark,
        dateBookmark = dateBookmark
    )
}

fun FolderWithMoviesDBO.toFolderFromFolderDBO(list: List<MovieDBO>): Folder{
    return Folder(
        id = folder.folderId,
        name = folder.folderName,
        color = folder.color,
        imageResName = folder.imageResName,
        listOfMovies = list.map { it.toMovie() }
    )
}

fun CollectionMovie.toCollection(): Collection{
    return Collection(
        id = id,
        name = name,
        slug = slug,
        category = category,
        viewedCount = viewedCount,
        moviesCount = moviesCount,
        createdAt = createdAt
    )
}