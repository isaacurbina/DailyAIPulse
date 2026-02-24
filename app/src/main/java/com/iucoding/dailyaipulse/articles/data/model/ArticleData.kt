package com.iucoding.dailyaipulse.articles.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleData(
	val source: SourceData,
	val author: String?,
	val title: String,
	val description: String?,
	val url: String,
	@param:Json(name = "urlToImage")
    val imageUrl: String?,
	@param:Json(name = "publishedAt)")
    val date: String,
	val content: String?
)
