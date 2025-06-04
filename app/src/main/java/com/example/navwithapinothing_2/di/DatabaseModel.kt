package com.example.navwithapinothing_2.di

import android.content.Context
import androidx.room.Room
import com.example.navwithapinothing_2.database.MovieDao
import com.example.navwithapinothing_2.database.MovieDatabase
import com.example.navwithapinothing_2.database.MovieDbConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Author: Vadim
 * @Date: 21.05.2025
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModel {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): MovieDatabase{
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            name ="db_movie_1"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDatabaseDao(database: MovieDatabase): MovieDao{
        return database.getMovieDao()
    }


}