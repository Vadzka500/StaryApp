package com.sidspace.stary.review.presentation.mapper

import com.sidspace.stary.domain.model.Review
import com.sidspace.stary.review.presentation.model.ReviewUi

fun Review.toReviewUi(): ReviewUi {
    return ReviewUi(
        id = id,
        title = title,
        type = type,
        review = review,
        date = date,
        author = author,
        reviewLikes = reviewLikes,
        reviewDislikes = reviewDislikes
    )
}