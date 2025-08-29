package com.sidspace.stary.movie.presentation.mapper

import com.sidspace.stary.domain.model.LocalMovie
import com.sidspace.stary.movie.presentation.model.LocalMovieUi

fun LocalMovie.toLocalMovieUi(): LocalMovieUi {
    return LocalMovieUi(
        movieId = movieId,
        isViewed = isViewed,
        dateViewed = dateViewed,
        isBookmark = isBookmark,
        dateBookmark = dateBookmark
    )
}
