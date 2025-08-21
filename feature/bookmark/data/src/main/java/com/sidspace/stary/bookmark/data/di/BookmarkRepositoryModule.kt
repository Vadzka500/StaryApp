package com.sidspace.stary.bookmark.data.di

import com.sidspace.stary.data.api.MovieApi
import com.sidspace.stary.data.database.MovieDao
import com.sidspace.stary.bookmark.data.repository.BookmarkRepositoryImpl
import com.sidspace.stary.bookmark.domain.repository.BookmarkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BookmarkRepositoryModule {

    @Provides
    fun provideBookmarkRepository(
        movieApi: MovieApi,
        movieDatabase: MovieDao
    ): BookmarkRepository = BookmarkRepositoryImpl(movieApi, movieDatabase)
}