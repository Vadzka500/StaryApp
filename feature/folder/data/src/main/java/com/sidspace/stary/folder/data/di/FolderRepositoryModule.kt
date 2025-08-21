package com.sidspace.stary.folder.data.di

import com.sidspace.stary.folder.data.repository.FolderRepositoryImpl
import com.sidspace.stary.folder.domain.repository.FolderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FolderRepositoryModule {

    @Binds
    abstract fun provideFolderRepository(
        impl : FolderRepositoryImpl
    ): FolderRepository
}