package com.sidspace.stary.bookmark.data.di

import com.sidspace.stary.bookmark.data.repository.BookmarkRepositoryImpl
import com.sidspace.stary.bookmark.domain.repository.BookmarkRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BookmarkRepositoryModule {

    @Binds
    abstract fun provideBookmarkRepository(
        impl: BookmarkRepositoryImpl
    ): BookmarkRepository
}
