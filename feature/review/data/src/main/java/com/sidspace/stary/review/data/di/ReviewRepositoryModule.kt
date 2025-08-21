package com.sidspace.stary.review.data.di

import com.sidspace.stary.review.data.repository.ReviewRepositoryImpl
import com.sidspace.stary.review.domain.repository.ReviewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ReviewRepositoryModule {

    @Binds
    abstract fun provideReviewRepository(
        impl : ReviewRepositoryImpl
    ): ReviewRepository
}