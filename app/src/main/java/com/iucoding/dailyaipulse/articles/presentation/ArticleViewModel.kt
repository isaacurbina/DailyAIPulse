package com.iucoding.dailyaipulse.articles.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iucoding.dailyaipulse.articles.data.model.ArticleData
import com.iucoding.dailyaipulse.articles.data.repository.ArticleRepository
import com.iucoding.dailyaipulse.articles.presentation.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArticleUiState>(ArticleUiState.Loading)
    val uiState: StateFlow<ArticleUiState> = _uiState.asStateFlow()

    init {
        loadArticles()
    }

    fun loadArticles() {
        emitState(ArticleUiState.Loading)

        viewModelScope.launch {
            try {
                val articlesData = articleRepository.getArticles()
                val articles = articlesData.map { it.toArticle() }
                emitState(ArticleUiState.Success(articles))
            } catch (throwable: Throwable) {
                Timber.e(throwable, "Error loading articles")
                emitState(
                    ArticleUiState.Error(
                        message = throwable.message ?: "Failed to load articles"
                    )
                )
            }
        }
    }

    private fun emitState(state: ArticleUiState) {
        Timber.d("ArticleViewModel emitting state: $state")
        _uiState.value = state
    }
}

private fun ArticleData.toArticle(): Article {
    return Article(
        sourceName = source.name,
        author = author,
        title = title,
        description = description,
        url = url,
        imageUrl = imageUrl,
        date = date,
        content = content
    )
}

