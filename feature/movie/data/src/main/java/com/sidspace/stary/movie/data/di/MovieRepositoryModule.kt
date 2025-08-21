package com.sidspace.stary.movie.data.di

import com.sidspace.stary.movie.data.repository.MovieRepositoryImpl
import com.sidspace.stary.movie.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieRepositoryModule {

    @Binds
    abstract fun provideMovieRepository(
        impl : MovieRepositoryImpl
    ): MovieRepository
}