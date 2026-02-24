package com.iucoding.dailyaipulse.articles.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iucoding.dailyaipulse.articles.data.model.ArticleData
import com.iucoding.dailyaipulse.articles.data.repository.ArticleRepository
import com.iucoding.dailyaipulse.articles.presentation.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
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
                        message = "Something went wrong, please try again later"
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

private fun formatDate(rawDate: String): String {
    return try {
        val articleDate = ZonedDateTime.parse(rawDate).toLocalDate()
        val today = LocalDate.now()
        val daysBetween = ChronoUnit.DAYS.between(articleDate, today)

        when (daysBetween) {
            0L -> "Today"
            1L -> "Yesterday"
            else -> "$daysBetween days ago"
        }
    } catch (e: Exception) {
        Timber.e(e, "Could not parse date")
        rawDate.substringBefore("T")
    }
}
