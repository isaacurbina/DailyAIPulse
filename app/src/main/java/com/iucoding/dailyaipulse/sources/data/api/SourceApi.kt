package com.iucoding.dailyaipulse.sources.data.api

import com.iucoding.dailyaipulse.sources.data.api.dto.SourcesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SourceApi {

    @GET("v2/top-headlines/sources")
    suspend fun getSources(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): SourcesResponseDto
}
