package com.iucoding.dailyaipulse.articles.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iucoding.dailyaipulse.ai.model.AiSummaryData
import com.iucoding.dailyaipulse.ai.repository.OpenAiRepository
import com.iucoding.dailyaipulse.articles.data.model.ArticleData
import com.iucoding.dailyaipulse.articles.data.repository.ArticleRepository
import com.iucoding.dailyaipulse.articles.presentation.model.AiSummary
import com.iucoding.dailyaipulse.articles.presentation.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
	private val articleRepository: ArticleRepository,
	private val openAiRepository: OpenAiRepository,
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

	fun getAISummary() {
		val currentState = _uiState.value
		if (currentState is ArticleUiState.Success) {
			if (currentState.isAiSummaryLoading) return

			_uiState.value = currentState.copy(
				isAiSummaryLoading = true,
				showAiSummary = false
			)

			viewModelScope.launch {
				try {
					val articleTitles = currentState.articles.map { it.title }
					val summaryData = openAiRepository.getArticleSummary(articleTitles)
					_uiState.value = currentState.copy(
						aiSummary = summaryData.toAiSummary(),
						isAiSummaryLoading = false,
						showAiSummary = true
					)
				} catch (throwable: Throwable) {
					Timber.e(throwable, "Error getting AI summary")
					_uiState.value = currentState.copy(isAiSummaryLoading = false)
				}
			}
		}
	}

	fun onAiSummaryDismissed() {
		val currentState = _uiState.value
		if (currentState is ArticleUiState.Success) {
			_uiState.value = currentState.copy(showAiSummary = false)
		}
	}

	fun onAiFabClicked() {
		val currentState = _uiState.value
		if (currentState is ArticleUiState.Success) {
			if (currentState.isAiSummaryLoading) return

			// Force a new request every time the FAB is clicked
			getAISummary()
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

private fun AiSummaryData.toAiSummary(): AiSummary {
	return AiSummary(
		summary = summary ?: "No summary available",
		investingSentiment = investingSentiment ?: "Neutral"
	)
}
