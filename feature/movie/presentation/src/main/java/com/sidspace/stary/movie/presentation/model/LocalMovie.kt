package com.sidspace.stary.movie.presentation.model


data class LocalMovieUi(
    val movieId: Long,
    var isViewed: Boolean = false,
    var dateViewed: Long? = null,
    var isBookmark: Boolean = false,
    var dateBookmark: Long? = null
)
