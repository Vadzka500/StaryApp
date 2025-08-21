package com.sidspace.stary.review.presentation.screen

import com.sidspace.stary.review.presentation.model.ReviewUi
import com.example.ui.model.ResultData




data class ReviewState(
    val reviews: ResultData<List<ReviewUi>> = ResultData.Loading,
    val countReviews: Int = 0,
)
