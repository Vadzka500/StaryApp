package com.sidspace.stary.review.presentation.screen

import com.sidspace.stary.review.presentation.model.ReviewUi

import com.sidspace.stary.ui.model.ResultData


data class ReviewState(
    val reviews: ResultData<List<ReviewUi>> = ResultData.Loading,
    val countReviews: Int = 0,
) {
    companion object {
        const val SHIMMER_ITEMS = 4
        const val MAX_REVIEW_LINE_COUNT = 4
    }
}
