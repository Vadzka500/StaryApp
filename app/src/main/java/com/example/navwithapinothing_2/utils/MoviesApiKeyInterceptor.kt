package com.example.navwithapinothing_2.utils

import com.example.navwithapinothing_2.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

internal class MoviesApiKeyInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("X-API-KEY", BuildConfig.MOVIES_API_KEY)
                .build()
        )
    }
}