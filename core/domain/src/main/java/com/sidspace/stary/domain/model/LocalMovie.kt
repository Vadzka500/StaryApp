package com.sidspace.stary.domain.model

data class LocalMovie(
    val movieId: Long,
    var isViewed: Boolean = false,
    var dateViewed: Long? = null,
    var isBookmark: Boolean = false,
    var dateBookmark: Long? = null
)
