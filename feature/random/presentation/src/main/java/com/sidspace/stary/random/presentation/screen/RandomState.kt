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
    val initialPage: Int = 1,
    val currentPageOffSetFraction: Float = 0f,
    val isSearch: Boolean = false,
    val isBadgeShown: Boolean = false
) {
    companion object {
        const val PAGER_OFFSET = 100F
        const val PAGER_DURATION = 3000
        const val DELAY_API_REQUEST = 500L
        val SCORE_RANGE = 1f..10f
        const val OLDEST_YEAR_OF_MOVIE = 1874F
        const val DELAY_REPEAT_ANIMATION_SCROLL = 1500L
        const val PAGER_HORIZONTAL_PADDING = 140
    }
}
