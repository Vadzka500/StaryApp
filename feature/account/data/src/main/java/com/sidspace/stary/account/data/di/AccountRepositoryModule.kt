package com.sidspace.stary.account.data.di

import com.sidspace.stary.account.data.repository.AccountRepositoryImpl
import com.sidspace.stary.account.domain.repository.AccountRepository
import com.sidspace.stary.data.api.MovieApi
import com.sidspace.stary.data.database.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AccountRepositoryModule {

    @Provides
    fun provideAccountRepository(
        movieApiService: MovieApi,
        movieDatabase: MovieDao
    ): AccountRepository = AccountRepositoryImpl(movieApiService, movieDatabase)
}
