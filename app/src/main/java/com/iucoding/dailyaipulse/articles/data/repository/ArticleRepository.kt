package com.iucoding.dailyaipulse.articles.data.repository

import com.iucoding.dailyaipulse.articles.data.api.ArticleApi
import com.iucoding.dailyaipulse.articles.data.api.dto.ArticleDto
import com.iucoding.dailyaipulse.articles.data.api.dto.SourceDto
import com.iucoding.dailyaipulse.articles.data.model.ArticleData
import com.iucoding.dailyaipulse.articles.data.model.SourceData
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val api: ArticleApi,
    @Named("news_api_key") private val apiKey: String
) {

    suspend fun getArticles(): List<ArticleData> {
        val response = api.getTopHeadlines(country = "us", category = "business", apiKey = apiKey)
        return response.articles?.map { it.toArticleData() } ?: emptyList()
    }
}

private fun ArticleDto.toArticleData(): ArticleData {
    return ArticleData(
        source = source.toSourceData(),
        author = author,
        title = title.orEmpty(),
        description = description,
        url = url.orEmpty(),
        imageUrl = urlToImage,
        date = publishedAt.orEmpty(),
        content = content
    )
}

private fun SourceDto?.toSourceData(): SourceData {
    return SourceData(
        id = this?.id,
        name = this?.name.orEmpty()
    )
}
