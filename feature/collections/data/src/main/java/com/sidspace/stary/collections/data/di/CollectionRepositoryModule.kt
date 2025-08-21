package com.sidspace.stary.collections.data.di

import com.sidspace.stary.collections.data.repository.CollectionRepositoryImpl
import com.sidspace.stary.collections.domain.repository.CollectionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CollectionRepositoryModule {

    @Binds
    abstract fun provideCollectionRepository(
        impl : CollectionRepositoryImpl
    ): CollectionRepository
}