package com.sidspace.stary.review.domain.usecase


import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.domain.model.Review
import com.sidspace.stary.review.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetReviewUseCase @Inject constructor(private val repository: ReviewRepository) {
    operator fun invoke(id: Long): Flow<Result<List<Review>>> = repository.getReviews(id)
}