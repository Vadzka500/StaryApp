package com.sidspace.stary.person.data.di

import com.sidspace.stary.person.data.repository.PersonRepositoryImpl
import com.sidspace.stary.person.domain.repository.PersonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PersonRepositoryModule {

    @Binds
    abstract fun providePersonRepository(
        impl : PersonRepositoryImpl
    ): PersonRepository
}