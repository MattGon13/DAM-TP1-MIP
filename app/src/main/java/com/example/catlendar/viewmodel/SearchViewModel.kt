package com.example.catlendar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.catlendar.model.CatEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<CatEvent> = emptyList()
)

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    // In a real app, this would query a repository/DAO
    fun updateSearchQuery(query: String, allEvents: List<CatEvent>) {
        val results = if (query.isBlank()) {
            emptyList()
        } else {
            allEvents.filter { it.title.contains(query, ignoreCase = true) }
        }
        
        _uiState.update {
            it.copy(
                searchQuery = query,
                searchResults = results
            )
        }
    }
}
