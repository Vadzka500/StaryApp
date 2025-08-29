package com.sidspace.stary.search.data.di

import com.sidspace.stary.search.data.repository.SearchRepositoryImpl
import com.sidspace.stary.search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchRepositoryModule {

    @Binds
    abstract fun provideSearchRepository(
        impl: SearchRepositoryImpl
    ): SearchRepository
}
