package com.sidspace.stary.random.data.di

import com.sidspace.stary.random.data.repository.RandomRepositoryImpl
import com.sidspace.stary.random.domain.repository.RandomRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RandomRepositoryModule {

    @Binds
    abstract fun provideRandomRepository(
        impl: RandomRepositoryImpl
    ): RandomRepository
}
