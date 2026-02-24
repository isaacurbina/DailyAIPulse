package com.iucoding.dailyaipulse.articles.presentation

import com.iucoding.dailyaipulse.articles.presentation.model.Article

sealed interface ArticleUiState {
    data object Loading : ArticleUiState
    data class Success(val articles: List<Article>) : ArticleUiState
    data class Error(val message: String) : ArticleUiState
}

