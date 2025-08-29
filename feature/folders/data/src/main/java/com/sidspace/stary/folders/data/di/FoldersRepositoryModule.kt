package com.sidspace.stary.folders.data.di

import com.sidspace.stary.folders.data.repository.FoldersRepositoryImpl
import com.sidspace.stary.folders.domain.repository.FoldersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FoldersRepositoryModule {

    @Binds
    abstract fun provideFoldersRepository(
        impl: FoldersRepositoryImpl
    ): FoldersRepository
}
