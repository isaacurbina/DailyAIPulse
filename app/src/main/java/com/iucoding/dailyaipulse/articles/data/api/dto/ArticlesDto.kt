package com.iucoding.dailyaipulse.articles.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SourceDto(
    val id: String? = null,
    val name: String? = null
)

@JsonClass(generateAdapter = true)
data class ArticleDto(
    val source: SourceDto? = null,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    @Json(name = "urlToImage") val urlToImage: String? = null,
    @Json(name = "publishedAt") val publishedAt: String? = null,
    val content: String? = null
)

@JsonClass(generateAdapter = true)
data class ArticlesResponseDto(
    val status: String? = null,
    val totalResults: Int? = null,
    val articles: List<ArticleDto>? = null
)
