package com.sidspace.stary.data.di

import android.content.Context
import androidx.room.Room
import com.sidspace.stary.data.database.MovieDao
import com.sidspace.stary.data.database.MovieDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModel {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): MovieDatabase {

        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            name ="db_movie_16"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDatabaseDao(database: MovieDatabase): MovieDao {
        return database.getMovieDao()
    }


}