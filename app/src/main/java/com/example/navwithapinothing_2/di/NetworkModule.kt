package com.example.navwithapinothing_2.di

import com.example.navwithapinothing_2.api.MovieApi
import com.example.navwithapinothing_2.utils.MoviesApiKeyInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(MoviesApiKeyInterceptor()).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val json = Json{
           /* coerceInputValues = true
            isLenient = true
            encodeDefaults = true*/

            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl("https://api.kinopoisk.dev/v1.4/")
            //.addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(provideOkHttpClient())
            .build()
    }

    @Provides
    @Singleton
    fun getService(): MovieApi {
        return provideRetrofit().create(MovieApi::class.java)
    }

}