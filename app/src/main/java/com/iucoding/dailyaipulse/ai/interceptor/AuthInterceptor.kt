package com.iucoding.dailyaipulse.ai.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Remove whitespace and potential surrounding quotes
        val sanitizedKey = apiKey.trim().removeSurrounding("\"").removeSurrounding("'")

        val authHeaderValue = if (sanitizedKey.startsWith("Bearer ", ignoreCase = true)) {
            sanitizedKey
        } else {
            "Bearer $sanitizedKey"
        }

        val request = chain.request().newBuilder()
            .header("Authorization", authHeaderValue)
            .build()
        return chain.proceed(request)
    }
}
