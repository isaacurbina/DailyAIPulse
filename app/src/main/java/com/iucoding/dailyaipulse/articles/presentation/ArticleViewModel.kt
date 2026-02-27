package com.iucoding.dailyaipulse.articles.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iucoding.dailyaipulse.ai.model.ChatRequest
import com.iucoding.dailyaipulse.ai.model.Message
import com.iucoding.dailyaipulse.ai.repository.OpenAiRepository
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

	suspend fun sendMessage(userMessage: String): String? {

		val request = ChatRequest(
			model = "gpt-4o-mini",
			messages = listOf(
				Message("system", "You are a helpful assistant."),
				Message("user", userMessage)
			),
			temperature = 0.7
		)

		val response = openAiRepository.askGpt(request)
		return response
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
