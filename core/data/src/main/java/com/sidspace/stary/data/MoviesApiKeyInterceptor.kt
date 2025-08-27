package com.sidspace.stary.data

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