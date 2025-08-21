package com.sidspace.stary.home.data.di

import com.sidspace.stary.home.data.repository.HomeRepositoryImpl
import com.sidspace.stary.home.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeRepositoryModule {

    @Binds
    abstract fun provideHomeRepository(
       impl: HomeRepositoryImpl
    ): HomeRepository
}