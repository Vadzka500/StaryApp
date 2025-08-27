package com.sidspace.stary.ui.mapper

import com.sidspace.stary.domain.model.Collection
import com.sidspace.stary.domain.model.Movie
import com.sidspace.stary.domain.model.Person
import com.sidspace.stary.ui.model.CollectionUi
import com.sidspace.stary.ui.model.MovieLightUi
import com.sidspace.stary.ui.model.MoviePreviewUi
import com.sidspace.stary.ui.model.MovieUi
import com.sidspace.stary.ui.model.PersonUi
import com.sidspace.stary.ui.model.TrailerUi


fun Movie.toMovieUiLight(): MovieLightUi {
    return MovieLightUi(
        id = id,
        name = name,
        enName = enName,
        previewUrl = previewUrl,
        score = kpScore
    )
}

fun Movie.toMovieData(): MovieUi {
    return MovieUi(
        id = id,
        name = name,
        enName = enName,
        previewUrl = previewUrl,
        scoreKp = kpScore,
        scoreImdb = imdbScore,
        isSeries = isSeries == true,
        year = year,
        countOfSeasons = seasonsInfo?.let { it ->
            if (it.isEmpty()) {
                1
            } else {
                it.count { season -> season.number != 0 }
            }
        } ?: 1,
        releaseStart = releaseYears?.firstOrNull()?.start,
        releaseEnd = releaseYears?.firstOrNull()?.end,
        listOfGenres = genres,
        description = description,
        backdrop = backdropUrl,
        persons = listOfPerson?.map { it.toPersonUi() },
        sequelsAndPrequels = sequelsAndPrequels?.map { it.toMovieData() },
        similarMovies = similarMovies?.map { it.toMovieData() },
        countries = countries,
        genres = genres,
        movieLength = movieLength,
        status = status,
        ageRating = ageRating,
        collections = listOfCollection,
        trailers = trailersUrl?.map { TrailerUi(it.name, it.url) }


    )
}

fun Movie.toMoviePreviewUi(): MoviePreviewUi {
    return MoviePreviewUi(
        id = id,
        name = name,
        enName = enName,
        previewUrl = previewUrl,
        score = kpScore,
        isSeries = isSeries == true,
        year = year,
        countOfSeasons = seasonsInfo?.let { it ->
            if (it.isEmpty()) {
                1
            } else {
                it.count { season -> season.number != 0 }
            }
        } ?: 1,
        releaseStart = releaseYears?.firstOrNull()?.start,
        releaseEnd = releaseYears?.firstOrNull()?.end,
        listOfGenres = genres,
    )
}

fun Person.toPersonUi(): PersonUi {
    return PersonUi(
        id = id,
        name = name,
        photo = photo,
        profession = profession,
        growth = growth,
        birthday = birthday,
        age = age,
        moviesIds = moviesIds,
    )
}

fun Collection.toCollectionUi(): CollectionUi {
    return CollectionUi(
        id = id,
        name = name,
        slug = slug,
        category = category,
        createdAt = createdAt,
        moviesCount = moviesCount,
        viewedCount = viewedCount
    )
}

fun MovieUi.toMoviePreviewUi(): MoviePreviewUi {
    return MoviePreviewUi(
        id = id,
        name = name,
        enName = enName,
        previewUrl = previewUrl,
        score = scoreKp,
        isSeries = isSeries,
        year = year,
        countOfSeasons = countOfSeasons,
        releaseStart = releaseStart,
        releaseEnd = releaseEnd,
        listOfGenres = listOfGenres,
    )
}

