package com.sidspace.stary.movie.presentation.screen

import com.sidspace.stary.ui.model.MovieData


sealed interface MovieIntent {
    data class LoadMovie(val id: Long): MovieIntent
    data class ToMovieScreen(val id:Long): MovieIntent
    data class ToPersonScreen(val id: Long): MovieIntent
    data class ToReviewScreen(val id: Long): MovieIntent
    data class PlayTrailer(val url: String): MovieIntent
    data class ViewedToMovie(val id:Long,val collections: List<String>?,val isViewed: Boolean): MovieIntent
    data class BookmarkToMovie(val id:Long,val collections: List<String>?,val isBookmark: Boolean): MovieIntent
    data class OnSelectFolder(val id: Long, val movie: MovieData): MovieIntent
    object ShowFoldersSheet: MovieIntent
    object HideFoldersSheet: MovieIntent
    object ShowTrailerSheet: MovieIntent
    object HideTrailerSheet: MovieIntent
    object OnError: MovieIntent
}