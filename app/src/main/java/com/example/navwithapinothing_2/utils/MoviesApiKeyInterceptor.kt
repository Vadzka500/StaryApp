package com.example.navwithapinothing_2.utils

import okhttp3.Interceptor
import okhttp3.Response

internal class MoviesApiKeyInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("X-API-KEY", "FF3PF1A-YQ6MXEK-NFQM9QD-76A6GH0")
                .build()
        )
    }
}