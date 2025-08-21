package com.sidspace.stary.review.data.repository

import com.sidspace.stary.data.api.MovieApi



import com.sidspace.stary.review.data.mapper.toReview
import com.sidspace.stary.data.utils.mapSuccess
import com.sidspace.stary.data.utils.safeCall
import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.domain.model.Review
import com.sidspace.stary.review.domain.repository.ReviewRepository
import com.sidspace.stary.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class ReviewRepositoryImpl @Inject constructor(private val movieApi: MovieApi) : ReviewRepository {

    override fun getReviews(id: Long): Flow<Result<List<Review>>> = flow {
        emit(safeCall { movieApi.getReviewsById(id) }.mapSuccess { it.docs }.toDomain { it.map{ it -> it.toReview() } })
    }
}