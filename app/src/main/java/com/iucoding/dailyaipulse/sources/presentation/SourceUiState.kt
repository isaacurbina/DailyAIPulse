package com.iucoding.dailyaipulse.sources.presentation

import com.iucoding.dailyaipulse.sources.presentation.model.Source

sealed interface SourceUiState {
    data object Loading : SourceUiState
    data class Success(val sources: List<Source>) : SourceUiState
    data class Error(val message: String) : SourceUiState
}
