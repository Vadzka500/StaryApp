package com.sidspace.stary.movie.presentation.screen

import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.movie.presentation.model.LocalMovieUi


import com.sidspace.stary.ui.model.MovieUi
import com.sidspace.stary.ui.model.ResultData


data class MovieState(
    val movie: ResultData<MovieUi> = ResultData.Loading,
    val isExistMovieDb: LocalMovieUi? = null,
    val filters: ResultData<List<Folder>> = ResultData.Loading,
    val isShowSheetFolders: Boolean = false,
    val isShowTrailerSheet: Boolean = false
)
