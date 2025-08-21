package com.sidspace.stary.review.domain.repository

import com.sidspace.stary.domain.model.Result
import com.sidspace.stary.domain.model.Review
import kotlinx.coroutines.flow.Flow


interface ReviewRepository {

    fun getReviews(id: Long): Flow<Result<List<Review>>>

}