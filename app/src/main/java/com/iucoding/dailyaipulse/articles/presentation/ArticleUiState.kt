package com.iucoding.dailyaipulse.articles.presentation

import com.iucoding.dailyaipulse.articles.presentation.model.AiSummary
import com.iucoding.dailyaipulse.articles.presentation.model.Article

sealed interface ArticleUiState {
    data object Loading : ArticleUiState
    data class Success(
        val articles: List<Article>,
        val aiSummary: AiSummary? = null,
        val isAiSummaryLoading: Boolean = false,
        val showAiSummary: Boolean = false
    ) : ArticleUiState
    data class Error(val message: String) : ArticleUiState
}
