package com.sidspace.stary.data.di

import com.sidspace.stary.data.api.MovieApi


import com.sidspace.stary.data.MoviesApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(MoviesApiKeyInterceptor()).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val json = Json {
           /* *//*  coerceInputValues = true
              isLenient = true
              encodeDefaults = true*/
            explicitNulls = true

            ignoreUnknownKeys = true
        }




        return Retrofit.Builder()
            .baseUrl("https://api.kinopoisk.dev/v1.4/")
            .addConverterFactory(GsonConverterFactory.create())
            //.addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(provideOkHttpClient())
            .build()
    }

    @Provides
    @Singleton
    fun getService(): MovieApi {
        return provideRetrofit().create(MovieApi::class.java)
    }

}