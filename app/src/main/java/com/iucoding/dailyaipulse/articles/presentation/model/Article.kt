package com.iucoding.dailyaipulse.articles.presentation.model

data class Article(
    val sourceName: String,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val date: String,
    val content: String?
)
