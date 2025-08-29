package com.sidspace.stary.data.di


import com.sidspace.stary.data.BuildConfig
import com.sidspace.stary.data.MoviesApiKeyInterceptor
import com.sidspace.stary.data.api.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val CONNECTION_TIMEOUT = 15L

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {

        return OkHttpClient.Builder().connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(MoviesApiKeyInterceptor()).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MOVIES_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
    }

    @Provides
    @Singleton
    fun getService(): MovieApi {
        return provideRetrofit().create(MovieApi::class.java)
    }

}
