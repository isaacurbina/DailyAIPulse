package com.iucoding.dailyaipulse.sources.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iucoding.dailyaipulse.sources.data.model.SourceData
import com.iucoding.dailyaipulse.sources.data.repository.SourceRepository
import com.iucoding.dailyaipulse.sources.presentation.model.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SourceViewModel @Inject constructor(
    private val sourceRepository: SourceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SourceUiState>(SourceUiState.Loading)
    val uiState: StateFlow<SourceUiState> = _uiState.asStateFlow()

    init {
        loadSources()
    }

    fun loadSources() {
        emitState(SourceUiState.Loading)

        viewModelScope.launch {
            try {
                val sourcesData = sourceRepository.getSources()
                val sources = sourcesData.map { it.toSource() }
                emitState(SourceUiState.Success(sources))
            } catch (throwable: Throwable) {
                Timber.e(throwable, "Error loading sources")
                emitState(
                    SourceUiState.Error(
                        message = "Something went wrong, please try again later"
                    )
                )
            }
        }
    }

    private fun emitState(state: SourceUiState) {
        Timber.d("SourceViewModel emitting state: $state")
        _uiState.value = state
    }
}

private fun SourceData.toSource(): Source {
    return Source(
        id = id,
        name = name,
        description = description
    )
}
