package com.sidspace.stary.viewed.data.di

import com.sidspace.stary.viewed.data.repository.ViewedRepositoryImpl
import com.sidspace.stary.viewed.domain.repository.ViewedRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ViewedRepositoryModule {

    @Binds
    abstract fun provideViewedRepository(
        impl: ViewedRepositoryImpl
    ): ViewedRepository
}
