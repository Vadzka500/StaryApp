package com.sidspace.stary.random.presentation.screen

import com.sidspace.stary.domain.RandomFiltersOption
import com.sidspace.stary.random.presentation.model.CollectionRandomUi

import com.sidspace.stary.ui.model.MoviePreviewUi
import com.sidspace.stary.ui.model.ResultData


data class RandomState(
    val isFiltersShown: Boolean = false,
    val isSearchButtonEnabled: Boolean = true,
    val filter: RandomFiltersOption = RandomFiltersOption(),
    val listOfCollections: ResultData<List<CollectionRandomUi>> = ResultData.Loading,
    val randomMovie: ResultData<MoviePreviewUi> = ResultData.None,
    val initialPage:Int = 1,
    val currentPageOffSetFraction: Float = 0f,
    val isSearch: Boolean = false,
    val isBadgeShown: Boolean = false
)

