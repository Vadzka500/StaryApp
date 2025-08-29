package com.sidspace.stary.collectionmovies.data.di

import com.sidspace.stary.collectionmovies.data.repository.CollectionMoviesRepositoryImpl
import com.sidspace.stary.collectionmovies.domain.repository.CollectionMoviesRepository
import com.sidspace.stary.data.api.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CollectionMovieRepositoryModule {

    @Provides
    fun provideCollectionMovieRepository(
        movieApi: MovieApi
    ): CollectionMoviesRepository = CollectionMoviesRepositoryImpl(movieApi)
}
