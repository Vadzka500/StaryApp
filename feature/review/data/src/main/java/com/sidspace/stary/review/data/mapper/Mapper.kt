package com.sidspace.stary.review.data.mapper

import com.sidspace.stary.data.model.api.review.UserReview
import com.sidspace.stary.domain.model.Review


fun UserReview.toReview(): Review {
    return Review(
        id = id,
        movieId = movieId,
        title = title,
        type = type,
        review = review,
        date = date,
        author = author,
        reviewLikes = reviewLikes,
        reviewDislikes = reviewDislikes
    )
}
