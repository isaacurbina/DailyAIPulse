package com.iucoding.dailyaipulse.articles.data.api

import com.iucoding.dailyaipulse.articles.data.api.dto.ArticlesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApi {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
		@Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): ArticlesResponseDto
}
